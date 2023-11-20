package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public boolean existsAccountByNumber(String number) {
        return accountRepository.existsByNumber(number);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Set<AccountDTO> getAllAccountsDto() {
        return getAllAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public AccountDTO getAccountDtoById(Long id) {
        return new AccountDTO(getAccountById(id));
    }

    @Override
    public Account findAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public Set<Account> findAccountsByClient(Client client) {
        return accountRepository.findByClient(client);
    }

    @Override
    public Set<AccountDTO> findAccountsDtoByClient(Client client) {
        return findAccountsByClient(client).stream().filter(account -> !account.getDelete()).map(AccountDTO::new).collect(Collectors.toSet());
    }

    @Override
    public boolean balanceLessThanEqualZero(Long id, Double balance) {
        return accountRepository.existsByIdAndBalanceLessThanEqual(id, balance);
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        account.setDelete(true);
        accountRepository.save(account);
    }

    @Override
    public int countByClientAndDelete(Client client) {
        return accountRepository.countByClientAndIsDelete(client, false);
    }

}
