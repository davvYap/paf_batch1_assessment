package sg.edu.nus.iss.batch1_assessment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.batch1_assessment.model.Transaction;
import sg.edu.nus.iss.batch1_assessment.repository.TransactionRepository;

@Service
public class LogAuditService {

    @Autowired
    private TransactionRepository transactionRepository;

    public void insertTransaction(Transaction transaction) {
        transactionRepository.insertTransaction(transaction);
    }
}
