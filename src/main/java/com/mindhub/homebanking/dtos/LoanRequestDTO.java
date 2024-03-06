package com.mindhub.homebanking.dtos;

public record LoanRequestDTO(String name,
                             double amount,
                             Integer installments,
                             String accountNumber) {
}
