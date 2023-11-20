package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDTO {
    private Long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    private AccountType accountType;
    private List<TransactionDTO> transactions;
    private Boolean isDelete;

    public AccountDTO(Account account){
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.accountType = account.getAccountType();
        this.transactions = account
                .getTransactions()
                .stream()
                .filter(transaction -> !transaction.getDelete())
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
        this.isDelete = account.getDelete();
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public Boolean getDelete() {
        return isDelete;
    }
}
