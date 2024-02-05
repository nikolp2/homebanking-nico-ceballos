package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.Client;

public class ClientDTO {

    private Long id;

    private String firstName;
    private String lastName;

    private String mail;




    public ClientDTO(){}

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

    public ClientDTO(Client client){
        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.mail = client.getMail();


    }
}
