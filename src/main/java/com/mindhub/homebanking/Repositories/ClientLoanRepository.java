package com.mindhub.homebanking.Repositories;

import com.mindhub.homebanking.Models.ClientLoan;
import com.mindhub.homebanking.Models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientLoanRepository extends JpaRepository<ClientLoan, Loan> {
}
