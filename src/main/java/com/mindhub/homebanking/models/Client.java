package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Boolean isAdmin;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    //CONSTRUCTORS
    public Client() {
    }

    public Client(String firstName, String lastName, String email, String password, Boolean isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    //GETTER ID
    public Long getId() {
        return id;
    }

    //GETTER & SETTERS
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    //GETTER @OneToMany
    public Set<Account> getAccounts() {
        return accounts;
    }

    //METHOD TO LINK ACCOUNT
    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);
    }

    //GETTER @OneToMany
    public List<Loan> getLoans() {
        return clientLoans.stream().map(e -> e.getLoan()).collect(Collectors.toList());
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    //FUNCTION TO LINK CLIENT-LOAN
    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    //GETTER @OneToMany
    public Set<Card> getCards() {
        return cards;
    }

    //FUNCTION TO LINK CARD
    public void addCard(Card card){
        card.setClient(this);
        cards.add(card);
    }

    //FUNCTION CARD-NAME
    public String nameCard(){
        return this.firstName+this.lastName;
    }
}
