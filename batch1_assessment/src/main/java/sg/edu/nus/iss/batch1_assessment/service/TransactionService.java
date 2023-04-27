package sg.edu.nus.iss.batch1_assessment.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.batch1_assessment.exception.TransactionException;
import sg.edu.nus.iss.batch1_assessment.model.Account;
import sg.edu.nus.iss.batch1_assessment.model.Transaction;
import sg.edu.nus.iss.batch1_assessment.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();

        SqlRowSet results = transactionRepository.getAllAccount();

        while (results.next()) {
            accounts.add(Account.convertFromSqlRowSet(results));
        }

        return accounts;
    }

    public Account getAccount(String id) {
        SqlRowSet rs = transactionRepository.getAccountById(id);
        while (rs.next()) {
            return Account.convertFromSqlRowSet(rs);
        }
        return null;
    }

    public boolean checkTransferSameAccount(String fromAccId, String toAccId) {
        if (fromAccId.equalsIgnoreCase(toAccId)) {
            return true;
        }
        return false;
    }

    public List<String> validateTransaction(Account sender, Account receiver, Transaction transaction) {
        List<String> errorMessages = new ArrayList<>();

        if (sender == null || receiver == null) {
            errorMessages.add("Sender or receiver account does not exist");
        }

        if (checkTransferSameAccount(sender.getAccountId(), receiver.getAccountId())) {
            errorMessages.add("Cannot transfer to the same account");
        }

        if (transaction.getAmount() <= 0) {
            errorMessages.add("Amount must be greater than 0");
        }

        if (transaction.getAmount() < 10.00) {
            errorMessages.add("Minimum transfer amount is 10.00");
        }

        if (transaction.getAmount() > sender.getBalance()) {
            errorMessages.add("Insufficient balance from sender account");
        }

        return errorMessages;
    }

    @Transactional(rollbackFor = TransactionException.class)
    public void performTransaction(Account sender, Account receiver, Transaction transaction) {

        String transID = UUID.randomUUID().toString().substring(0, 8);
        transaction.setTransactionId(transID);

        transactionRepository.withdraw(sender.getAccountId(), transaction.getAmount());
        transactionRepository.deposit(receiver.getAccountId(), transaction.getAmount());
    }

    // EXTRA
    public Collection<Document> insertAccountToMongoDB(List<Account> accounts) {
        return transactionRepository.insertAccountToMongoDB(accounts);
    }

    public String deleteAllAccountInMongoDB() {
        return transactionRepository.deleteAllAccountInMongoDB();
    }

    public Account getAccountTransactionHistory(String id) {
        Optional<List<Document>> documents = transactionRepository.getAccountTransactionHistory(id);
        if (documents.isEmpty()) {
            return null;
        }
        List<Document> transactions = documents.get();
        Document selectedAccount = transactions.get(0);

        return Account.convertFromDocument(selectedAccount);
    }
}
