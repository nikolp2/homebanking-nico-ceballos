package com.mindhub.homebanking;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Models.Transaction;
import com.mindhub.homebanking.Models.TransactionType;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.Repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;


@SpringBootApplication
public class HomebankingApplication {
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
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

            Transaction transaction1 = new Transaction(-5000.34, "pay", LocalDateTime.now(), TransactionType.DEBIT);

			account1.addTransaction(transaction1);
			transactionRepository.save(transaction1);


		};



	}
}
