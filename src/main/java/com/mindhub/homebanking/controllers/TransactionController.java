package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.implement.AccountServiceImpl;
import com.mindhub.homebanking.services.implement.ClientServiceImpl;
import com.mindhub.homebanking.services.implement.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.mindhub.homebanking.models.TransactionType.*;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private ClientServiceImpl clientService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private TransactionServiceImpl transactionService;

    @Transactional
    @PostMapping("/clients/current/transactions")
    public ResponseEntity<String> newTransaction(Authentication authentication, @RequestParam Double amount, @RequestParam String description, @RequestParam String originAccount, @RequestParam String destinationAccount){

        Account fromAccount = accountService.findAccountByNumber(originAccount);
        Account toAccount = accountService.findAccountByNumber(destinationAccount);
        Client currentClient = clientService.findClientByEmail(authentication.getName());

        if ( amount <= 0 ) {
            return new ResponseEntity<>("The amount must be greater than zero", HttpStatus.FORBIDDEN);
        }

        if(description.isEmpty() || description.isBlank()){
            return new ResponseEntity<>("Enter a description", HttpStatus.FORBIDDEN);
        }

        if(originAccount.isEmpty() || originAccount.isBlank()){
            return new ResponseEntity<>("Enter a source account", HttpStatus.FORBIDDEN);
        }

        if(destinationAccount.isEmpty() || destinationAccount.isBlank()){
            return new ResponseEntity<>("Choose a target account", HttpStatus.FORBIDDEN);
        }

        if(originAccount.equals(destinationAccount)){
            return new ResponseEntity<>("You cannot transfer to the same account.", HttpStatus.FORBIDDEN);
        }

        if (!accountService.existsAccountByNumber(originAccount) && !accountService.existsAccountByNumber(destinationAccount)){
            return new ResponseEntity<>("The entered account does not exist.", HttpStatus.FORBIDDEN);
        }

        if (!currentClient.getAccounts().contains(fromAccount)){
            return new ResponseEntity<>("The account does not belong to the customer.", HttpStatus.FORBIDDEN);
        }

        if (fromAccount.getBalance() < amount){
            return new ResponseEntity<>("You do not have sufficient funds to make the transfer.", HttpStatus.FORBIDDEN);
        }

        Transaction fromTransaction = new Transaction(DEBIT, -amount, description + " to " + destinationAccount, LocalDateTime.now(), (fromAccount.getBalance() - amount));
        fromAccount.addTransaction(fromTransaction);
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        transactionService.saveTransaction(fromTransaction);


        Transaction toTransaction = new Transaction(CREDIT, amount, description + " from " + originAccount, LocalDateTime.now(), (toAccount.getBalance() + amount));
        toAccount.addTransaction(toTransaction);
        toAccount.setBalance(toAccount.getBalance() + amount);
        transactionService.saveTransaction(toTransaction);

        return new ResponseEntity<>("The transaction was successful.", HttpStatus.CREATED);
    }
}
