package sg.edu.nus.iss.batch1_assessment.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.batch1_assessment.model.Account;
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

}
