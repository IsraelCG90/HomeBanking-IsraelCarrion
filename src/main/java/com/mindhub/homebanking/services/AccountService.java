package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;
import java.util.Set;

public interface AccountService {

    boolean existsAccountByNumber(String number);

    void saveAccount(Account account);

    List<Account> getAllAccounts();

    Set<AccountDTO> getAllAccountsDto();

    Account getAccountById(Long id);

    AccountDTO getAccountDtoById(Long id);

    Account findAccountByNumber(String email);

    Set<Account> findAccountsByClient(Client client);
    Set<AccountDTO> findAccountsDtoByClient(Client client);
}
