package sg.edu.nus.iss.batch1_assessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import sg.edu.nus.iss.batch1_assessment.model.Account;
import sg.edu.nus.iss.batch1_assessment.model.Transaction;
import sg.edu.nus.iss.batch1_assessment.service.TransactionService;
import java.util.List;

@Controller
public class TransactionController {

    @Autowired
    TransactionService transactionService;

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

        if (sender == null || receiver == null) {
            ObjectError oe = new ObjectError("globalError", "Account not found");
            binding.addError(oe);
        }

        if (transactionService.checkTransferSameAccount(sender.getAccountId(), receiver.getAccountId())) {
            ObjectError oe = new ObjectError("globalError", "Cannot transfer to the same account");
            binding.addError(oe);
        }

        if (transaction.getAmount() <= 0) {
            ObjectError oe = new ObjectError("globalError", "Amount must be greater than 0");
            binding.addError(oe);
        }

        if (transaction.getAmount() < 10.00) {
            ObjectError oe = new ObjectError("globalError", "Minimum transfer amount is 10.00");
            binding.addError(oe);
        }

        if (transaction.getAmount() > sender.getBalance()) {
            ObjectError oe = new ObjectError("globalError", "Insufficient balance from sender account");
            binding.addError(oe);
        }

        if (binding.hasErrors()) {
            List<Account> accounts = transactionService.getAllAccounts();
            model.addAttribute("accounts", accounts);
            model.addAttribute("transaction", transaction);
            return "view0";
        }

        return "view1";
    }
}
