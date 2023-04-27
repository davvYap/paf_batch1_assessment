package sg.edu.nus.iss.batch1_assessment.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class TransactionByType {
    private String type;
    private double totalAmount;
    private List<String> transactionsId = new ArrayList<>();
    private int transactionCount;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<String> getTransactionsId() {
        return transactionsId;
    }

    public void setTransactionsId(List<String> transactionsId) {
        this.transactionsId = transactionsId;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    @Override
    public String toString() {
        return "TransactionByType [type=" + type + ", totalAmount=" + totalAmount + ", transactionsId=" + transactionsId
                + ", transactionCount=" + transactionCount + "]";
    }

    public static TransactionByType convertFromDocument(Document d) {
        TransactionByType t = new TransactionByType();
        t.setType(d.getString("_id"));
        t.setTotalAmount(d.getDouble("total_amount"));
        t.setTransactionCount(d.getInteger("transaction_count"));
        t.setTransactionsId(d.getList("transactions", String.class));
        return t;
    }

}
