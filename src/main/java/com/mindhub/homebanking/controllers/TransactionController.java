package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.mindhub.homebanking.models.TransactionType.*;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/clients/current/transactions")
    public ResponseEntity<Object> newCard(Authentication authentication, @RequestParam Double amount, @RequestParam String description, @RequestParam String originAccount, @RequestParam String destinationAccount){

        Account fromAccount = accountRepository.findByNumber(originAccount);
        Account toAccount = accountRepository.findByNumber(destinationAccount);
        Client currentClient = clientRepository.findByEmail(authentication.getName());

        if ( amount <= 0 ) {
            return new ResponseEntity<>("The amount must be greater than zero", HttpStatus.FORBIDDEN);
        }

        if(description.isEmpty() || description.isBlank() || originAccount.isEmpty() || originAccount.isBlank() || destinationAccount.isEmpty() || destinationAccount.isBlank()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(originAccount.equals(destinationAccount)){
            return new ResponseEntity<>("You cannot transfer to the same account.", HttpStatus.FORBIDDEN);
        }

        if (!accountRepository.existsByNumber(originAccount) && !accountRepository.existsByNumber(destinationAccount)){
            return new ResponseEntity<>("The entered account does not exist.", HttpStatus.FORBIDDEN);
        }

        if (!currentClient.getAccounts().contains(fromAccount)){
            return new ResponseEntity<>("The account does not belong to the customer.", HttpStatus.FORBIDDEN);
        }

        if (fromAccount.getBalance() < amount){
            return new ResponseEntity<>("You do not have sufficient funds to make the transfer.", HttpStatus.FORBIDDEN);
        }

        Transaction fromTransaction = new Transaction(DEBITT, -amount, description + " to " + destinationAccount, LocalDateTime.now());
        fromAccount.addTransaction(fromTransaction);
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        transactionRepository.save(fromTransaction);


        Transaction toTransaction = new Transaction(CREDITT, amount, description + " from " + originAccount, LocalDateTime.now());
        toAccount.addTransaction(toTransaction);
        toAccount.setBalance(toAccount.getBalance() + amount);
        transactionRepository.save(toTransaction);

        return new ResponseEntity<>("The transaction was successful.", HttpStatus.CREATED);
    }
}
