package sg.edu.nus.iss.batch1_assessment.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.validation.constraints.Size;

public class Account implements Serializable {

    @Size(min = 10)
    private String accountId;
    private String name;
    private double balance;

    List<Transaction> transfer = new ArrayList<>();
    List<Transaction> receive = new ArrayList<>();

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getNameConcatAccountId() {
        return "%s (%s)".formatted(name, accountId);
    }

    public List<Transaction> getTransfer() {
        return transfer;
    }

    public void setTransfer(List<Transaction> transfer) {
        this.transfer = transfer;
    }

    public List<Transaction> getReceive() {
        return receive;
    }

    public void setReceive(List<Transaction> receive) {
        this.receive = receive;
    }

    @Override
    public String toString() {
        return "Account [accountId=" + accountId + ", name=" + name + ", balance=" + balance + ", transfer=" + transfer
                + ", receive=" + receive + "]";
    }

    public static Account convertFromSqlRowSet(SqlRowSet rs) {
        Account a = new Account();
        a.setAccountId(rs.getString("account_id"));
        a.setName(rs.getString("name"));
        a.setBalance(rs.getBigDecimal("balance").doubleValue());
        return a;
    }

    public JsonObject toJsonObject() {
        return Json.createObjectBuilder()
                .add("accountId", this.accountId)
                .add("name", this.name)
                .add("balance", this.balance)
                .build();
    }

    public static Account convertFromDocument(Document d) {
        Account a = new Account();
        a.setAccountId(d.getString("accountId"));
        a.setName(d.getString("name"));
        a.setBalance(d.getDouble("balance"));

        List<Document> transferList = d.getList("transfer", Document.class);

        List<Document> receiveList = d.getList("receive", Document.class);

        a.setTransfer(transferList.stream().map(t -> Transaction.convertFromDocument(t)).toList());
        a.setReceive(receiveList.stream().map(t -> Transaction.convertFromDocument(t)).toList());
        return a;
    }

}
