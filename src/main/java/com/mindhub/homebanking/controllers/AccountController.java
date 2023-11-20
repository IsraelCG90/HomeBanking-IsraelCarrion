package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.AccountUtils.accountNumber;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/accounts")
    public Set<AccountDTO> getAccounts(){
        return accountService.getAllAccountsDto();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccountDtoById(id);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<String> newAccount(Authentication authentication, @RequestParam String accountType){
        if (accountType.isBlank() || !(accountType.equals("CURRENT") || accountType.equals("SAVING"))) {
            return new ResponseEntity<>("You must select a valid account type.", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.findClientByEmail(authentication.getName());

        if(accountService.countByClientAndDelete(client) >= 3){
            return new ResponseEntity<>("Cannot create any more accounts for this client", HttpStatus.FORBIDDEN);
        }

        String accountNumber;
        do {
            accountNumber = accountNumber();
        } while (accountService.existsAccountByNumber(accountNumber));

        Account account = new Account(accountNumber, LocalDate.now(), 0, AccountType.valueOf(accountType));
        client.addAccount(account);
        clientService.saveClient(client);
        accountService.saveAccount(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccount(Authentication authentication) {
        return accountService.findAccountsDtoByClient(clientService.findClientByEmail(authentication.getName()));
    }

    @PostMapping("/clients/current/accounts/delete")
    public ResponseEntity<String> deleteCard(@RequestParam Long id){
        if (!accountService.balanceLessThanEqualZero(id, 0.0)){
            return new ResponseEntity<>("You must have your balance at zero in order to delete your account.", HttpStatus.FORBIDDEN);
        }
        accountService.deleteAccount(id);
        transactionService.deleteByAccountId(accountService.getAccountById(id));

        return new ResponseEntity<>("The Account was successfully eliminated", HttpStatus.OK);
    }

}
