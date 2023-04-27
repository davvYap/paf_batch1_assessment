package sg.edu.nus.iss.batch1_assessment.model;

import java.io.Serializable;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.validation.constraints.Size;

public class Account implements Serializable {

    @Size(min = 10)
    private String accountId;
    private String name;
    private double balance;

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

    @Override
    public String toString() {
        return "Account [accountId=" + accountId + ", name=" + name + ", balance=" + balance + "]";
    }

    public static Account convertFromSqlRowSet(SqlRowSet rs) {
        Account a = new Account();
        a.setAccountId(rs.getString("account_id"));
        a.setName(rs.getString("name"));
        a.setBalance(rs.getBigDecimal("balance").doubleValue());
        return a;
    }

}
