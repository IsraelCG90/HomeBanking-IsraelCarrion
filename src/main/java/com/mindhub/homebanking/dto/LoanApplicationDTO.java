package com.mindhub.homebanking.dto;

public class LoanApplicationDTO {
    private Long id;
    private Double amount;
    private Integer payments;
    private String toAccount;

    //CONSTRUCTOR
    public LoanApplicationDTO(Long id, Double amount, Integer payments, String toAccount) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.toAccount = toAccount;
    }

    //GETTERS
    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getToAccount() {
        return toAccount;
    }
}
