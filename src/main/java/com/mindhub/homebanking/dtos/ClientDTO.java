package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Card;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Models.ClientLoan;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String mail;
    private Set<AccountDTO> accounts;
    private Set<ClientLoanDTO> loans;
    private Set<CardDTO> cards;

    public ClientDTO(Client client){
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.mail = client.getMail();
        this.accounts = accountDTOS(client.getAccounts());
        this.loans = clientLoanDTOS(client.getClientLoanSet());
        this.cards = cardDTOS(client.getCardSet());
    }

    private Set<ClientLoanDTO> clientLoanDTOS(Set<ClientLoan> clientLoanSet) {
        return clientLoanSet.stream().map(ClientLoanDTO::new).collect(Collectors.toSet());
    }

    private Set<AccountDTO> accountDTOS(Set<Account> accounts){
        return accounts.stream().map(AccountDTO::new).collect(Collectors.toSet());

    }

    private Set<CardDTO> cardDTOS(Set<Card> cards){
        return cards.stream().map(CardDTO::new).collect(Collectors.toSet());
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

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<AccountDTO> accounts) {
        this.accounts = accounts;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public void setLoans(Set<ClientLoanDTO> loans) {
        this.loans = loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }

    public void setCards(Set<CardDTO> cards) {
        this.cards = cards;
    }
}
