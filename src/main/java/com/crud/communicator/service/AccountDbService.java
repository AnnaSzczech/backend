package com.crud.communicator.service;

import com.crud.communicator.domain.AccountEntity;
import com.crud.communicator.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountDbService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountEntity save(final AccountEntity account){
        return accountRepository.save(account);
    }

    public Optional<AccountEntity> findAccountByLogin(final String login){
        return accountRepository.findByLogin(login);
    }

    public Optional<AccountEntity> findAccount(String login, String password){
        return accountRepository.retrieveAccount(login, password);
    }

    public Optional<AccountEntity> findAccountWithTheSameLoginOrEmail(String login, String email){
        return accountRepository.retrieveAccountWithTheSameEmailOrLogin(login, email);
    }

    public void deleteAccount(final String login){
        accountRepository.deleteByLogin(login);
    }

    public List<AccountEntity> findLogins(){
        return accountRepository.findAll();
    }

    public List<AccountEntity> findAccounts(){
        return accountRepository.findAll();
    }
}
