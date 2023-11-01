package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @RequestMapping("/loan")
    public Set<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(l -> new LoanDTO(l)).collect(Collectors.toSet());
    }
    @RequestMapping(value="/loan", method=RequestMethod.POST)
    public ResponseEntity<String> addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        if (loanApplicationDTO.getAmount() <= 0) {
            return new ResponseEntity<>("The amount must be greater than zero.", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("The amount must be greater than zero.", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getToAccount().isBlank()) {
            return new ResponseEntity<>("You must enter an account number.", HttpStatus.FORBIDDEN);
        }

        if (!loanRepository.existsById(loanApplicationDTO.getId())) {
            return new ResponseEntity<>("Credit does not exist.", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanRepository.findById(loanApplicationDTO.getId()).orElse(null);

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("The amount exceeds the maximum allowed..", HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("The number of payments does not exist.", HttpStatus.FORBIDDEN);
        }

        if (!accountRepository.existsByNumber(loanApplicationDTO.getToAccount())) {
            return new ResponseEntity<>("The target account does not exist.", HttpStatus.FORBIDDEN);
        }

        Account account = accountRepository.findByNumber(loanApplicationDTO.getToAccount());

        Client client = clientRepository.findByEmail(authentication.getName());

        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("The destination account does not belong to the customer.", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan((loanApplicationDTO.getAmount() * 1.20), loanApplicationDTO.getPayments());
        client.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);
        clientLoanRepository.save(clientLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() + " loan approved.", LocalDateTime.now());
        account.addTransaction(transaction);
        transactionRepository.save(transaction);

        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
        accountRepository.save(account);

      return new ResponseEntity<>("Credit approved", HttpStatus.CREATED);
    }

}
