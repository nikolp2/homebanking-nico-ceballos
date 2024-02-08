package com.mindhub.homebanking.Models;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private double maxAmount;

    @ElementCollection
    private Set<Integer> payment = new HashSet<>();

    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoanSet =new HashSet<>();

    public Loan(){}

    public Loan(String name, double maxAmount, Set<Integer> payment) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payment = payment;
    }

    public Set<ClientLoan> getClientLoanSet() {
        return clientLoanSet;
    }

    public void setClientLoanSet(Set<ClientLoan> clientLoanSet) {
        this.clientLoanSet = clientLoanSet;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Set<Integer> getPayment() {
        return payment;
    }

    public void setPayment(Set<Integer> payment) {
        this.payment = payment;
    }

    @Embeddable
    public class Payment{
        private double maxAmount;
        private int installment;
    }
}
