package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.dtos.ClientDTO;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.user(), loginDTO.password()));

            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.user());

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

        return ResponseEntity.ok(client);
    }



    @GetMapping("/test")
    public ResponseEntity<?> test(){
        String mail = SecurityContextHolder.getContext().getAuthentication().getName();

        return  ResponseEntity.ok("hello" +mail);
    }
}
