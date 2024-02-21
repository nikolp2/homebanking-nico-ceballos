package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.dtos.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/")
    public ResponseEntity<List<AccountDTO>> getAllAccounts(){

        List<Account> accounts = accountRepository.findAll();

        return new ResponseEntity<>(accounts.stream()
                                            .map(AccountDTO::new)
                                            .toList(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable("id") Long id){

        Account account = accountRepository.findById(id).orElse(null);

        if(account == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new AccountDTO(account), HttpStatus.OK);
    }
}
