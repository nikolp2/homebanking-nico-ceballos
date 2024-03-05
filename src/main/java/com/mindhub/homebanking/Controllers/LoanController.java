package com.mindhub.homebanking.Controllers;


import com.mindhub.homebanking.Models.Loan;
import com.mindhub.homebanking.Repositories.LoanRepository;
import com.mindhub.homebanking.dtos.LoanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("api/loans")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/type")
    public ResponseEntity<List<LoanDTO>> getAllLoans(){
        List<Loan> loans = loanRepository.findAll();

        return new ResponseEntity<>(loans.stream()
                                         .map(LoanDTO::new)
                                         .collect(Collectors.toList())
                                         , HttpStatus.OK);
    }



}
