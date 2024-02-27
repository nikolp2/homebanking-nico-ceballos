package com.mindhub.homebanking.Models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName, lastName, mail, password;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoanSet = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Card> cards = new HashSet<>();

    public Client(){};

    public Client(String firstName, String lastName, String mail, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.password = password;
    }

    public void setClientLoanSet(Set<ClientLoan> clientLoanSet) {
        this.clientLoanSet = clientLoanSet;
    }

    public Set<Account> getAccounts(){
        return accounts;
    }

    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);

    }



    public Set<ClientLoan> getClientLoanSet() {
        return clientLoanSet;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Long getId() {
        return id;
    }

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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setCardSet(Set<Card> cardSet) {
        this.cards = cardSet;
    }

    public Set<Card> getCardSet() {
        return cards;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
