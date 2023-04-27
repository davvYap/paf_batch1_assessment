package sg.edu.nus.iss.batch1_assessment.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.batch1_assessment.model.Account;
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

}
