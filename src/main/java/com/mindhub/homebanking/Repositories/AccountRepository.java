package com.mindhub.homebanking.Repositories;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
   Boolean findByNumber(String number);

   Boolean existsAccountByNumberAndClient(String number, Client client);

   Account findAccountByNumberAndClient(String number, Client client);

   Account findAccountByNumber(String number);
}
