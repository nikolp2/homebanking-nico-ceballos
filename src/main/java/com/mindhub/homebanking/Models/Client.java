package com.mindhub.homebanking.Models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "native", strategy = "native")

    private Long Id;
    private String firstName, lastName, mail;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();



    public Client(){};



    public Client(String firstName, String lastName, String mail){
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
    }

    public Set<Account> getAccounts(){
        return accounts;
    }

    public void addAccount(Account account){
        account.setOwner(this);
        accounts.add(account);
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
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

    public String toString(){
        return firstName + " " + lastName;
    }

    /*@Override
    public String toString() {
        return "Client{" +
                "Id=" + Id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }*/

}
