package com.mindhub.homebanking;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.Repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;


@SpringBootApplication
public class HomebankingApplication {
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository){
		return args -> {

			Client client1 =new Client( "Melba", "Morel", "melba@mindhub.com");
			Client client2 =new Client( "Sonrisas", "Sonya", "sonrisas@mindhub.com");
			Client client3 =new Client( "Tentación", "Roberto", "tentacion@mindhub.com");




            clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);

			Account account1=new Account("12", LocalDate.now(), 550.23);

            client1.addAccount(account1);
            accountRepository.save(account1);

            Transaction transaction1 = new Transaction(-5000.34, "E-bay", LocalDateTime.now(), TransactionType.DEBIT);

			account1.addTransaction(transaction1);
			transactionRepository.save(transaction1);

			Loan loan1 = new Loan("Mortgage", 500000, Set.of(12,24,36,48,60));
			Loan loan2 = new Loan("Personal", 100000, Set.of(6,12,24));
			Loan loan3 = new Loan("Automotive", 300000, Set.of(6,12,24,36));

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan newLoan1 = new ClientLoan(400000, 60);
			ClientLoan newLoan2 = new ClientLoan(50000, 12);

			newLoan1.setClient(client1);
			newLoan2.setClient(client1);

			newLoan1.setLoan(loan1);
			newLoan2.setLoan(loan2);

			clientLoanRepository.save(newLoan1);
			clientLoanRepository.save(newLoan2);

		};



	}
}
