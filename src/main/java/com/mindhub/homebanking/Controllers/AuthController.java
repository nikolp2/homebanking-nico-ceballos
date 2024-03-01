package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.dtos.LoginDTO;
import com.mindhub.homebanking.dtos.RegisterDTO;
import com.mindhub.homebanking.services.JwtUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository AccountRepository;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));

            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.email());

            final String jwt = jwtUtilService.generateToken(userDetails);

            return ResponseEntity.ok(jwt);

        }catch (Exception event){
            return  new ResponseEntity<>("MAl", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO){
        Client client = new Client(
                registerDTO.getFirstName(),
                registerDTO.getLastName(),
                registerDTO.getMail(),
                passwordEncoder.encode(registerDTO.getPassword())
        );

        clientRepository.save(client);

        String accountNumber = "VIN-" + String.format("%08d", new Random().nextInt(1000000000));

        if (!AccountRepository.findByNumber(accountNumber)){
            Account account = new Account(accountNumber, LocalDate.now(), 0.0);

            account.setClient(client);
            AccountRepository.save(account);
        }

        return ResponseEntity.ok("user created successfully!" + client);
    }



    @GetMapping("/test")
    public ResponseEntity<?> test(){
        String mail = SecurityContextHolder.getContext().getAuthentication().getName();

        return  ResponseEntity.ok("hello" + mail);
    }
}
