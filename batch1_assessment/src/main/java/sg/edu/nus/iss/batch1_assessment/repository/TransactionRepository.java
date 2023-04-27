package sg.edu.nus.iss.batch1_assessment.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;

import sg.edu.nus.iss.batch1_assessment.model.Account;
import sg.edu.nus.iss.batch1_assessment.model.Transaction;
import sg.edu.nus.iss.batch1_assessment.model.TransactionByType;

import static sg.edu.nus.iss.batch1_assessment.repository.DBQueries.*;

@Repository
public class TransactionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    public SqlRowSet getAllAccount() {
        return jdbcTemplate.queryForRowSet(GET_ALL_ACCOUNTS);
    }

    public SqlRowSet getAccountById(String id) {
        return jdbcTemplate.queryForRowSet(GET_ACCOUNT_BY_ID, id);
    }

    public boolean withdraw(String senderId, double amount) {
        int row = jdbcTemplate.update(UPDATE_ACCOUNT_BALANCE_TRANSFER, amount, senderId);
        return row > 0;
    }

    public boolean deposit(String receiverId, double amount) {
        int row = jdbcTemplate.update(UPDATE_ACCOUNT_BALANCE_RECEIVE, amount, receiverId);
        return row > 0;
    }

    public void insertTransaction(Transaction transaction) {
        mongoTemplate.insert(Document.parse(transaction.toJsonObject().toString()), "transaction");
    }

    // EXTRA
    public Collection<Document> insertAccountToMongoDB(List<Account> accounts) {
        List<Document> documents = accounts.stream()
                .map(acc -> Document.parse(acc.toJsonObject().toString()))
                .toList();

        return mongoTemplate.insert(documents, "accounts");
    }

    public String deleteAllAccountInMongoDB() {
        Query query = Query.query(Criteria.where("accountId").exists(true));
        DeleteResult rs = mongoTemplate.remove(query, "accounts");
        return rs.toString();
    }

    public Optional<List<Document>> getAccountTransactionHistory(String id) {
        MatchOperation mOp = Aggregation.match(Criteria.where("accountId").is(id));
        LookupOperation lOpTransfer = Aggregation.lookup("transaction", "accountId", "from_Account_Id", "transfer");
        LookupOperation lOpReceive = Aggregation.lookup("transaction", "accountId", "to_Account_Id", "receive");

        Aggregation pipeline = Aggregation.newAggregation(mOp, lOpTransfer, lOpReceive);
        AggregationResults<Document> rs = mongoTemplate.aggregate(pipeline, "accounts", Document.class);

        List<Document> documents = rs.getMappedResults();
        return Optional.ofNullable(documents);
    }

    public Optional<List<Document>> getTransactionByType() {
        LookupOperation lOpTransfer = Aggregation.lookup("accounts", "from_Account_Id", "accountId",
                "transfer_account");
        LookupOperation lOpReceive = Aggregation.lookup("accounts", "to_Account_Id", "accountId", "receive_acount");
        ProjectionOperation pOp = Aggregation.project().andExclude("transfer_account._id")
                .andExclude("receive_acount._id");

        GroupOperation gOp = Aggregation.group("type")
                .sum("amount").as("total_amount")
                .push("transaction_Id").as("transactions")
                .count().as("transaction_count");
        SortOperation sOp = Aggregation.sort(Sort.by(Direction.ASC, "_id"));

        Aggregation pipeline = Aggregation.newAggregation(lOpTransfer, lOpReceive, pOp, gOp, sOp);
        AggregationResults<Document> rs = mongoTemplate.aggregate(pipeline, "transaction", Document.class);

        return Optional.ofNullable(rs.getMappedResults());
    }
}
