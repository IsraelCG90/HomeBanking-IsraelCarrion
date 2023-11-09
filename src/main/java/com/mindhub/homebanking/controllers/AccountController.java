package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.implement.AccountServiceImpl;
import com.mindhub.homebanking.services.implement.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.Set;


@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private ClientServiceImpl clientService;

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String accountNumber() {
        StringBuilder accountNumber;
        do {
            accountNumber = new StringBuilder();
            for (byte i = 0; i <= 2; i++) {
                accountNumber.append(getRandomNumber(0, 9));
            }
        } while (accountService.existsAccountByNumber("VIN" + accountNumber));
        return "VIN" + accountNumber;
    }

    @RequestMapping("/accounts")
    public Set<AccountDTO> getAccounts(){
        return accountService.getAllAccountsDto();
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccountDtoById(id);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<String> newAccount(Authentication authentication){
        Client client = clientService.findClientByEmail(authentication.getName());

        if(client.getAccounts().size() >= 3){
            return new ResponseEntity<>("Cannot create any more accounts for this client", HttpStatus.FORBIDDEN);
        }

        Account account = new Account(accountNumber(), LocalDate.now(), 0);
        client.addAccount(account);
        clientService.saveClient(client);
        accountService.saveAccount(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccount(Authentication authentication) {
        return accountService.findAccountsDtoByClient(clientService.findClientByEmail(authentication.getName()));
    }

}
