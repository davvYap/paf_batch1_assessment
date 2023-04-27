package sg.edu.nus.iss.batch1_assessment.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Transaction implements Serializable {
    private String transactionId;
    private String fromAccountId;
    private String toAccountId;
    private double amount;
    private String comment;
    private String type;
    private String date;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction [fromAccountId=" + fromAccountId + ", toAccountId=" + toAccountId + ", amount=" + amount
                + ", comment=" + comment + ", type=" + type + "]";
    }

    public JsonObject toJsonObject() {
        return Json.createObjectBuilder()
                .add("transaction_Id", this.transactionId)
                .add("date", getDateTime())
                .add("from_Account_Id", this.fromAccountId)
                .add("to_Account_Id", this.toAccountId)
                .add("amount", this.amount)
                .add("comment", this.comment)
                .add("type", this.type)
                .build();
    }

    public static String getDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String date = now.format(formatter);
        return date;
    }

    public static Transaction convertFromDocument(Document d) {
        Transaction t = new Transaction();
        t.setTransactionId(d.getString("transaction_Id"));
        t.setDate(d.getString("date"));
        t.setFromAccountId(d.getString("from_Account_Id"));
        t.setToAccountId(d.getString("to_Account_Id"));
        t.setAmount(d.getDouble("amount"));
        t.setComment(d.getString("comment"));
        t.setType(d.getString("type"));
        return t;
    }

}
