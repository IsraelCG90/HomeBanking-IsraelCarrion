package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.implement.AccountServiceImpl;
import com.mindhub.homebanking.services.implement.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.mindhub.homebanking.utils.AccountUtils.accountNumber;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientServiceImpl clientService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.getAllClientsDto();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClientDtoById(id);
    }

    @PostMapping("/clients")
    public ResponseEntity<String> newClient(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password) {

        if (firstName.isBlank()) {
            return new ResponseEntity<>("Missing First Name", HttpStatus.FORBIDDEN);
        }

        if(lastName.isBlank()){
            return new ResponseEntity<>("Missing Last Name", HttpStatus.FORBIDDEN);
        }

        if(email.isBlank()){
            return new ResponseEntity<>("Missing Email", HttpStatus.FORBIDDEN);
        }

        if(password.isBlank()){
            return new ResponseEntity<>("Missing Password", HttpStatus.FORBIDDEN);
        }

        if (!clientService.existsClientByEmail(email)) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName,email, passwordEncoder.encode(password), false);
        clientService.saveClient(client);

        String accountNumber;
        do {
            accountNumber = accountNumber();
        } while (accountService.existsAccountByNumber(accountNumber));

        Account account = new Account(accountNumber, LocalDate.now(), 0, AccountType.CURRENT);
        client.addAccount(account);

        accountService.saveAccount(account);

        return new ResponseEntity<>("Client created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getClientCurrent(Authentication authentication){
        return new ClientDTO(clientService.findClientByEmail(authentication.getName()));
    }

}
