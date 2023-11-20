package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Client;

import java.util.List;
import java.util.stream.Collectors;

public class ClientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<AccountDTO> accounts;
    private List<ClientLoanDTO> loans;
    private List<CardDTO> cards;

    //CONSTRUCTOR
    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client
                .getAccounts()
                .stream()
                .filter(account -> !account.getDelete())
                .map(AccountDTO::new)
                .collect(Collectors.toList());
        this.loans = client
                .getClientLoans()
                .stream()
                .map(e -> new ClientLoanDTO(e))
                .collect(Collectors.toList());
        this.cards = client
                .getCards()
                .stream()
                .filter(card -> !card.delete())
                .map(e -> new CardDTO(e))
                .collect(Collectors.toList());
    }

    //GETTERS
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<AccountDTO> getAccounts() {
        return accounts;
    }

    public List<ClientLoanDTO> getLoans() {
        return loans;
    }

    public List<CardDTO> getCards() {
        return cards;
    }

}
