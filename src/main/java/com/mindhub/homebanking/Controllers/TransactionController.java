package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Models.Transaction;
import com.mindhub.homebanking.Models.TransactionType;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.Repositories.TransactionRepository;
import com.mindhub.homebanking.dtos.NewTransactionDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/")
    public ResponseEntity<?> newTransaction(@RequestBody NewTransactionDTO newTransactionDTO){
        String userMail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client currentClient = clientRepository.findByEmail(userMail);

        if(!accountRepository.findByNumber(newTransactionDTO.numberDestinationAcc())) {
            return new ResponseEntity<>("the destination account does not exist", HttpStatus.FORBIDDEN);
        }

        if(!accountRepository.findByNumber(newTransactionDTO.numberOriginAcc())) {
            return new ResponseEntity<>("the origin account does not exist", HttpStatus.FORBIDDEN);
        }

        if(newTransactionDTO.numberDestinationAcc().isBlank()) {
            return new ResponseEntity<>("The destination number field must not be empty " , HttpStatus.FORBIDDEN);
        }

        if(newTransactionDTO.numberOriginAcc().isBlank()) {
            return new ResponseEntity<>("The origin number field must not be empty " , HttpStatus.FORBIDDEN);
        }

        if(newTransactionDTO.amount() == null || newTransactionDTO.amount().toString().isBlank()) {
            return new ResponseEntity<>("The mount field must not be empty " , HttpStatus.FORBIDDEN);
        }

        if(newTransactionDTO.description().isBlank()) {
            return new ResponseEntity<>("The description field must not be empty " , HttpStatus.FORBIDDEN);
        }

        if(!accountRepository.existsAccountByNumberAndClient(newTransactionDTO.numberOriginAcc(), currentClient)) {
            return new ResponseEntity<>("You are not the owner of the source account", HttpStatus.FORBIDDEN);
        }

        if(newTransactionDTO.amount() <= 0) {
            return new ResponseEntity<>("You cannot transfer an amount equal to or less than zero", HttpStatus.FORBIDDEN);
        }

        if(newTransactionDTO.numberOriginAcc().equals(newTransactionDTO.numberDestinationAcc())) {
            return new ResponseEntity<>("the account numbers are the same, enter a different account destination number", HttpStatus.FORBIDDEN);
        }

        if(accountRepository.findAccountByNumberAndClient(newTransactionDTO.numberOriginAcc(), currentClient)
                .getBalance() < newTransactionDTO.amount()) {
            return new ResponseEntity<>("You do not have enough amount to carry out the transaction", HttpStatus.FORBIDDEN);
        }

        Transaction transactionDebit = new Transaction(newTransactionDTO.amount(), newTransactionDTO.description(), LocalDateTime.now(), TransactionType.DEBIT);

        Transaction transactionCredit = new Transaction(newTransactionDTO.amount(), newTransactionDTO.description(), LocalDateTime.now(), TransactionType.CREDIT);

        Account accountOrigin = accountRepository.findAccountByNumberAndClient(newTransactionDTO.numberOriginAcc(), currentClient);
        accountOrigin.setBalance(accountOrigin.getBalance() - newTransactionDTO.amount());
        accountOrigin.addTransaction(transactionDebit);

        Account accountDestination = accountRepository.findAccountByNumber(newTransactionDTO.numberDestinationAcc());
        accountDestination.setBalance(accountDestination.getBalance() + newTransactionDTO.amount());
        accountDestination.addTransaction(transactionCredit);

        accountRepository.save(accountOrigin);
        accountRepository.save(accountDestination);

        transactionRepository.save(transactionDebit);
        transactionRepository.save(transactionCredit); //Probar acá con null

        return new ResponseEntity<>("transaction created successfully", HttpStatus.CREATED);
    }


}
