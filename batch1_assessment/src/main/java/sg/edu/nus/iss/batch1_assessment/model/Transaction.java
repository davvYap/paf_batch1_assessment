package sg.edu.nus.iss.batch1_assessment.model;

import java.io.Serializable;

public class Transaction implements Serializable {
    private String fromAccountId;
    private String toAccountId;
    private double amount;
    private String comment;

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

    @Override
    public String toString() {
        return "Transaction [fromAccountId=" + fromAccountId + ", toAccountId=" + toAccountId + ", amount=" + amount
                + ", comment=" + comment + "]";
    }

}
