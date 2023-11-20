package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientLoanDTO;
import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.implement.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientServiceImpl clientService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private LoanServiceImpl loanService;
    @Autowired
    private ClientLoanServiceImpl clientLoanService;
    @Autowired
    private TransactionServiceImpl transactionService;

    @GetMapping("/loan")
    public Set<LoanDTO> getLoans() {
        return loanService.getAllLoanDto();
    }

    @PostMapping("/loan")
    public ResponseEntity<String> addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        if (loanApplicationDTO.getAmount() <= 0) {
            return new ResponseEntity<>("The amount must be greater than zero.", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("You need to select a payment.", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getToAccount().isBlank()) {
            return new ResponseEntity<>("You must enter an account number.", HttpStatus.FORBIDDEN);
        }

        if (!loanService.existsLoanById(loanApplicationDTO.getId())) {
            return new ResponseEntity<>("Credit does not exist.", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanService.findLoanById(loanApplicationDTO.getId());

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("The amount exceeds the maximum allowed..", HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("The number of payments does not exist.", HttpStatus.FORBIDDEN);
        }

        if (!accountService.existsAccountByNumber(loanApplicationDTO.getToAccount())) {
            return new ResponseEntity<>("The target account does not exist.", HttpStatus.FORBIDDEN);
        }

        Account account = accountService.findAccountByNumber(loanApplicationDTO.getToAccount());

        Client client = clientService.findClientByEmail(authentication.getName());

        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("The destination account does not belong to the customer.", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(((loanApplicationDTO.getAmount() * loan.getInterest())/100) + loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments());
        client.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);
        clientLoanService.saveClientLoan(clientLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() + " loan approved.", LocalDateTime.now(), (account.getBalance() + loanApplicationDTO.getAmount()));
        account.addTransaction(transaction);
        transactionService.saveTransaction(transaction);

        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
        accountService.saveAccount(account);

      return new ResponseEntity<>("Credit approved", HttpStatus.CREATED);
    }

    @PostMapping("/loan/create")
    public ResponseEntity<String> createLoan(@RequestParam String name, @RequestParam Double maxAmount, @RequestParam Double interest, @RequestParam List<Integer> payments) {
        if (name.isBlank()){
            return new ResponseEntity<>("You must enter a loan name.", HttpStatus.FORBIDDEN);
        }
        if (loanService.existsLoanByName(name)){
            return new ResponseEntity<>("The loan name already exists.", HttpStatus.FORBIDDEN);
        }
        if (maxAmount <= 0){
            return new ResponseEntity<>("The amount must be greater than zero.", HttpStatus.FORBIDDEN);
        }
        if (interest <= 0){
            return new ResponseEntity<>("The interest must be greater than zero.", HttpStatus.FORBIDDEN);
        }
        if (payments.toArray().length == 0){
            return new ResponseEntity<>("You need to include at least one payment.", HttpStatus.FORBIDDEN);
        }

        Loan loan = new Loan(name, maxAmount, interest, payments);
        loanService.saveLoan(loan);

        return new ResponseEntity<>("Loan created", HttpStatus.CREATED);
    }

    @PostMapping("/loan/payment")
    public ResponseEntity<String> payLoan(@RequestParam Double paymentMade, @RequestParam Integer paymentSelected, @RequestParam Long idAccount, @RequestParam Long idLoan , @RequestParam String nameLoan){
        if (paymentMade <= 0){
            return new ResponseEntity<>("The payment must be greater than zero.", HttpStatus.FORBIDDEN);
        }

        Account account = accountService.getAccountById(idAccount);

        if (account.getBalance() < paymentMade){
            return new ResponseEntity<>("You do not have sufficient funds to make the payment.", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = clientLoanService.findLoanById(idLoan);

        Transaction transaction = new Transaction(TransactionType.DEBIT, -paymentMade, "Payment " + paymentSelected + " installments of the " + nameLoan + " loan.", LocalDateTime.now(), account.getBalance()-paymentMade);
        clientLoan.setPaymentsMade(clientLoan.getPaymentsMade() + paymentSelected);
        account.setBalance(account.getBalance()-paymentMade);
        account.addTransaction(transaction);

        transactionService.saveTransaction(transaction);
        clientLoanService.saveClientLoan(clientLoan);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Successful payment.", HttpStatus.CREATED);
    }
}
