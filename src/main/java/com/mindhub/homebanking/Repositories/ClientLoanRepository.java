package com.mindhub.homebanking.Repositories;

import com.mindhub.homebanking.Models.ClientLoan;
import com.mindhub.homebanking.Models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientLoanRepository extends JpaRepository<ClientLoan, Loan> {
}
