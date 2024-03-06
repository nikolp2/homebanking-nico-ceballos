package com.mindhub.homebanking.Controllers;


import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.Repositories.*;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.dtos.LoanRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("api/loans")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @GetMapping("/")
    public ResponseEntity<?> getAllLoans(){

        List<Loan> loans = loanRepository.findAll();

        return new ResponseEntity<> (loans, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/")
    public ResponseEntity<?> requestNewLoan(@RequestBody LoanRequestDTO loanRequestDTO){

        String mail = SecurityContextHolder.getContext().getAuthentication().getName();

        Client client = clientRepository.findByEmail(mail);

        Loan selectedLoan = loanRepository.findByName(loanRequestDTO.name());

        int interest = (int) (loanRequestDTO.amount() * 0.2);

        if(client == null){
            return new ResponseEntity<>("Client not found", HttpStatus.FORBIDDEN);
        }

        if (loanRequestDTO.name() == null){
            return new ResponseEntity<>("Name is required", HttpStatus.FORBIDDEN);
        }

        if (loanRequestDTO.amount() == 0){
            return new ResponseEntity<>("The amount of the loan is required", HttpStatus.FORBIDDEN);
        }

        if (loanRequestDTO.installments() == null){
            return new ResponseEntity<>("Number of installments are required", HttpStatus.FORBIDDEN);
        }

        if (loanRequestDTO.accountNumber() == null){
            return new ResponseEntity<>("Account number is required", HttpStatus.FORBIDDEN);
        }

        if(!accountRepository.existsByNumber(loanRequestDTO.accountNumber())){
            return new ResponseEntity<>("Account not found", HttpStatus.FORBIDDEN);
        }

        if(!accountRepository.existsAccountByNumberAndClient(loanRequestDTO.accountNumber(), client)){
            return new ResponseEntity<>("The selected account does not belong to you", HttpStatus.FORBIDDEN);
        }

        if (selectedLoan == null){
            return new ResponseEntity<>("Loan not found", HttpStatus.FORBIDDEN);
        }

        if (selectedLoan.getMaxAmount() < loanRequestDTO.amount()){
            return new ResponseEntity<>("Amount exceeds the maximum amount", HttpStatus.FORBIDDEN);
        }

        if (!selectedLoan.getPayment().contains(loanRequestDTO.installments())){
            return new ResponseEntity<>("Invalid number of installments", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(loanRequestDTO.amount(),
                                              (loanRequestDTO.installments() + interest));


        client.addClientLoan(clientLoan);
        selectedLoan.addClientLoan(clientLoan);

        Account loanAccountSelected = accountRepository.findAccountByNumberAndClient(loanRequestDTO.accountNumber(), client);

        Transaction loanTransaction = new Transaction(loanRequestDTO.amount(),
                "Loan Approved", LocalDateTime.now(), TransactionType.CREDIT);

        loanAccountSelected.addTransaction(loanTransaction);

        clientRepository.save(client);
        loanRepository.save(selectedLoan);
        clientLoanRepository.save(clientLoan);
        accountRepository.save(loanAccountSelected);

        return new ResponseEntity<>("Loan requested successfully", HttpStatus.CREATED);
    }
}
