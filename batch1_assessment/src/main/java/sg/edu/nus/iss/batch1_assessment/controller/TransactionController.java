package sg.edu.nus.iss.batch1_assessment.controller;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.json.Json;
import jakarta.validation.Valid;
import sg.edu.nus.iss.batch1_assessment.exception.TransactionException;
import sg.edu.nus.iss.batch1_assessment.model.Account;
import sg.edu.nus.iss.batch1_assessment.model.Transaction;
import sg.edu.nus.iss.batch1_assessment.service.LogAuditService;
import sg.edu.nus.iss.batch1_assessment.service.TransactionService;

import java.util.Collection;
import java.util.List;

@Controller
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    LogAuditService logAuditService;

    @GetMapping(path = "/")
    public String getHomePage(Model model, @ModelAttribute Transaction transaction) {
        List<Account> accounts = transactionService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        model.addAttribute("transaction", transaction);
        return "view0";
    }

    @PostMapping(path = "/transfer", consumes = "application/x-www-form-urlencoded")
    public String transfer(@ModelAttribute @Valid Transaction transaction, Model model, BindingResult binding) {

        Account sender = transactionService.getAccount(transaction.getFromAccountId());
        Account receiver = transactionService.getAccount(transaction.getToAccountId());

        List<String> errorMessages = transactionService.validateTransaction(sender, receiver, transaction);

        // if (sender == null || receiver == null) {
        // ObjectError oe = new ObjectError("globalError", "Account not found");
        // binding.addError(oe);
        // }

        // if (transactionService.checkTransferSameAccount(sender.getAccountId(),
        // receiver.getAccountId())) {
        // ObjectError oe = new ObjectError("globalError", "Cannot transfer to the same
        // account");
        // binding.addError(oe);
        // }

        // if (transaction.getAmount() <= 0) {
        // ObjectError oe = new ObjectError("globalError", "Amount must be greater than
        // 0");
        // binding.addError(oe);
        // }

        // if (transaction.getAmount() < 10.00) {
        // ObjectError oe = new ObjectError("globalError", "Minimum transfer amount is
        // 10.00");
        // binding.addError(oe);
        // }

        // if (transaction.getAmount() > sender.getBalance()) {
        // ObjectError oe = new ObjectError("globalError", "Insufficient balance from
        // sender account");
        // binding.addError(oe);
        // }

        if (!errorMessages.isEmpty()) {
            for (String errMessage : errorMessages) {
                binding.addError(new ObjectError("globalError", errMessage));
            }
        }

        if (binding.hasErrors()) {
            List<Account> accounts = transactionService.getAllAccounts();
            model.addAttribute("accounts", accounts);
            model.addAttribute("transaction", transaction);
            return "view0";
        }

        transactionService.performTransaction(sender, receiver, transaction);
        logAuditService.insertTransaction(transaction);
        return "view1";
    }

    // EXTRA
    @GetMapping(path = "/insertToMongo")
    public ResponseEntity<String> insertAccountsToMongo() {
        List<Account> accounts = transactionService.getAllAccounts();
        Collection<Document> insertedDocs = transactionService.insertAccountToMongoDB(accounts);
        if (insertedDocs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("ERROR", "No accounts inserted")
                            .build().toString());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Json.createObjectBuilder()
                        .add("SUCCESS", "%d accounts inserted".formatted(insertedDocs.size()))
                        .build().toString());
    }

    @GetMapping(path = "/deleteAccFromMongo")
    public ResponseEntity<String> delteAllAccountsToMongo() {
        String rs = transactionService.deleteAllAccountInMongoDB();
        if (rs == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("ERROR", "No accounts deleted")
                            .build().toString());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Json.createObjectBuilder()
                        .add("SUCCESS", "%s accounts deleted".formatted(rs))
                        .build().toString());
    }

    @GetMapping(path = "/updateMongoAccounts")
    public String updateMongoDBAccounts(Model model, @ModelAttribute Transaction transaction) {
        List<Account> accounts = transactionService.getAllAccounts();
        transactionService.deleteAllAccountInMongoDB();
        transactionService.insertAccountToMongoDB(accounts);

        model.addAttribute("accounts", accounts);
        model.addAttribute("transaction", transaction);
        return "view0";
    }

    @GetMapping(path = "/checkTransactions")
    public String checkTransactions(Model model) {
        List<Account> accounts = transactionService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "view2";
    }

    @GetMapping(path = "/getTransactions")
    public String geTransactions(Model model, @RequestParam String accountId) {

        List<Account> accounts = transactionService.getAllAccounts();
        model.addAttribute("accounts", accounts);

        Account account = transactionService.getAccountTransactionHistory(accountId);
        model.addAttribute("account", account);
        return "view2";
    }
}
