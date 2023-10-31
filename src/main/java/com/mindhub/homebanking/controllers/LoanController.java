package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @PostMapping()
    public ResponseEntity<Object> newLoan(Authentication authentication, @RequestParam Double amount, @RequestParam Integer payments, @RequestParam String loan, @RequestParam String toAccount){
        if (amount <= 0){
            return new ResponseEntity<>("The loan must be greater than zero", HttpStatus.FORBIDDEN);
        }
        if (payments <= 0){
            return new ResponseEntity<>("The number of payments must be greater than zero.", HttpStatus.FORBIDDEN);
        }
        ClientLoan clientLoan = new ClientLoan(amount, payments);
        return new ResponseEntity<>("Loan created successfully", HttpStatus.CREATED);
    }

}
