package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.ClientLoan;

import java.util.Set;

public class ClientLoanDTO {
    private long id;
    private double amount;
    private int payments;
    private long loanId;
    private String loanName;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.loanId = clientLoan.getLoan().getId();
        this.loanName = clientLoan.getLoan().getName();
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getLoanName() {
        return loanName;
    }
}
