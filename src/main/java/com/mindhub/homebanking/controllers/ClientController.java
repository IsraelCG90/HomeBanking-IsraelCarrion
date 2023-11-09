package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientServiceImpl clientService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.getAllClientsDto();
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClientDtoById(id);
    }

    //CREATE NUMBER ACCOUNT
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

        Account account = new Account(accountNumber(), LocalDate.now(), 0);
        client.addAccount(account);

        accountService.saveAccount(account);

        return new ResponseEntity<>("Client created successfully", HttpStatus.CREATED);
    }

    @RequestMapping("/clients/current")
    public ClientDTO getClientCurrent(Authentication authentication){
        return new ClientDTO(clientService.findClientByEmail(authentication.getName()));
    }

}
