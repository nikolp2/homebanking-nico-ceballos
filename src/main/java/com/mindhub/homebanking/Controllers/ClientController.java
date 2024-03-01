package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Card;
import com.mindhub.homebanking.Models.CardType;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.TypeAndColorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllClients(){

        List<Client> clients = clientRepository.findAll();

        return new ResponseEntity<>(clients.stream().map(ClientDTO::new).collect(java.util.stream.Collectors.toList()), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable("id") Long id){
        Client client = clientRepository.findById(id).orElse(null);
        if(client == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        ClientDTO clientDTO = new ClientDTO(client);
        return new ResponseEntity<>(clientDTO, HttpStatus.OK);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getClient(){
        String userMail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(userMail);

        return ResponseEntity.ok(new ClientDTO(client));
    }

    @PostMapping("/current/accounts")
    public ResponseEntity<?> createAccount(@RequestBody AccountDTO accountDTO){
        String userMail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(userMail);

        // Checkeamos sí tiene menos de 3 cuentas
        if (client.getAccounts().size() >= 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have 3 registered accounts, you can't create more.");
        }

        // Generar un número de cuenta aleatorio
        String accountNumber = generateRandomAccountNumber();

        // Crear una nueva cuenta
        Account account = new Account();
        account.setNumber(accountNumber);
        account.setBalance(0);
        account.setClient(client);

        // Guardar la cuenta a través del repositorio de cuentas
        accountRepository.save(account);

        return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully!");
    }
    private String generateRandomAccountNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(90000000) + 10000000; // Generar un número aleatorio de 8 dígitos
        return "VIN-" + randomNumber;
    }

    @GetMapping("/current/accounts")
    public ResponseEntity<?> getAllAccountsForClient() {
        String userMail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(userMail);

        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
        }

        Set<Account> accounts = client.getAccounts();

        return ResponseEntity.ok(accounts.stream().map(AccountDTO::new).collect(Collectors.toList()));
    }

    @PostMapping("/current/cards")
    public ResponseEntity<?> createCard(@RequestBody TypeAndColorDTO typeAndColorDTO) {
        // Obtener la información del cliente autenticado
        String userMail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(userMail);

        // Contar el número de tarjetas de débito y crédito del cliente
        int debitCardCount = (int) client.getCardSet().stream()
                .filter(card -> card.getType() == CardType.DEBIT)
                .count();
        int creditCardCount = (int) client.getCardSet().stream()
                .filter(card -> card.getType() == CardType.CREDIT)
                .count();

        // Verificar si el cliente ya tiene una tarjeta de débito
        if (typeAndColorDTO.cardType() == CardType.DEBIT && debitCardCount >= 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You already have a debit card");
        }

        // Verificar si el cliente ya tiene tres tarjetas de crédito
        if (typeAndColorDTO.cardType() == CardType.CREDIT && creditCardCount >= 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("yoy already have 3 credit cards");
        }

        // Verificar si el cliente ya tiene una tarjeta del color especificado
        boolean hasCardWithColor = client.getCardSet().stream()
                .anyMatch(card -> card.getColor() == typeAndColorDTO.cardColor());
        if (hasCardWithColor) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You already have a: " + typeAndColorDTO.cardColor() + " card.");
        }

        // Generar los detalles de la tarjeta
        long generateCardNumber = Long.parseLong(generateCardNumber());
        String cardholder = client.getFirstName() + " " + client.getLastName();
        int cvv = generateCVV();
        LocalDate startDate = LocalDate.now();
        LocalDate expirationDate = startDate.plusYears(5);

        // Crear una nueva tarjeta
        Card card = new Card(client, typeAndColorDTO.cardType(), typeAndColorDTO.cardColor(),  generateCardNumber, cvv, startDate, expirationDate);
        client.addCard(card);

        // Guardar la tarjeta en la lista de tarjetas del cliente
        clientRepository.save(client);

        // Construir y devolver el DTO de la tarjeta creada
        CardDTO cardDTO = new CardDTO(card);
        return ResponseEntity.status(HttpStatus.CREATED).body(cardDTO);
    }

    // Métodos para generar un número de tarjeta y un CVV aleatorios
    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cardNumber.append(random.nextInt(10));
            }
            if (i < 3) {
                cardNumber.append("-");
            }
        }
        return cardNumber.toString();
    }

    private int generateCVV() {
        Random random = new Random();
        return 100 + random.nextInt(900); // Genera un número aleatorio de 3 dígitos
    }




}


