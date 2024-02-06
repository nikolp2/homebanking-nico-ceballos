package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String mail;

    private Set<AccountDTO> accounts;

    public ClientDTO(Client client){
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.mail = client.getMail();
        this.accounts = accountDTOS(client.getAccounts());
    }


    public ClientDTO(){}

    private Set<AccountDTO> accountDTOS(Set<Account> accounts){
        return accounts.stream().map(AccountDTO::new).collect(Collectors.toSet());

    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMail() {
        return mail;
    }


    public Set<AccountDTO> getAccounts() {
        return accounts;
    }
}
