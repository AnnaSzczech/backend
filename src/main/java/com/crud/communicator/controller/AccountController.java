package com.crud.communicator.controller;

import com.crud.communicator.controller.exceptions.AccountNotFoundException;
import com.crud.communicator.controller.exceptions.NotUniqueEmailOrLoginException;
import com.crud.communicator.domain.AccountEntity;
import com.crud.communicator.domain.dto.AccountDto;
import com.crud.communicator.domain.dto.LoginDto;
import com.crud.communicator.mapper.AccountMapper;
import com.crud.communicator.service.AccountDbService;
import com.crud.communicator.service.MessageDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    @Autowired
    private AccountDbService accountDbService;

    @Autowired
    private MessageDbService messageDbService;

    @Autowired
    private AccountMapper accountMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public void addAccount(@RequestBody AccountDto accountDto){
        if (checkIfEmailOrLoginIsUnique(accountDto.getEmail(), accountDto.getLogin())) {
            accountDbService.save(accountMapper.mapToAccount(accountDto));
        } else {
            throw new NotUniqueEmailOrLoginException();
        }
    }

    @GetMapping
    public List<LoginDto> getLogins() {
        return accountMapper.mapToLoginDtoList(accountDbService.findLogins());
    }

    @GetMapping(value = "/{login}&{password}")
    public AccountDto getAccount(@PathVariable String login, @PathVariable String password) {
        return accountMapper.mapToAccountDto(accountDbService.findAccount(login, password).orElseThrow(AccountNotFoundException::new));
    }

    @DeleteMapping(value = "/{login}")
    public void deleteAccount(@PathVariable String login) {
        if (!accountDbService.findAccountByLogin(login).isPresent()) {
            throw new AccountNotFoundException();
        }
        messageDbService.deleteMessages(login);
        accountDbService.deleteAccount(login);

    }

    @PutMapping(value = "/logIn/{login}&{password}")
    public void logIn(@PathVariable String login, @PathVariable String password) {
        AccountEntity accountEntity = accountDbService.findAccount(login, password).orElseThrow(AccountNotFoundException::new);
        accountEntity.setOnline(true);
        accountDbService.save(accountEntity);
    }

    @PutMapping(value = "/logOut/{login}")
    public void logOut(@PathVariable String login) {
        AccountEntity accountEntity = accountDbService.findAccountByLogin(login).orElseThrow(AccountNotFoundException::new);
        accountEntity.setOnline(false);
        accountDbService.save(accountEntity);
    }

    @PutMapping(value = "changePassword/{login}&{newPassword}")
    public void changePassword(@PathVariable String login, @PathVariable String newPassword){
        if (!accountDbService.findAccountByLogin(login).isPresent()) {
            throw new AccountNotFoundException();
        }
        AccountEntity accountEntity = accountDbService.findAccountByLogin(login).get();
        accountEntity.setPassword(newPassword);
        accountDbService.save(accountEntity);
    }

    @GetMapping(value = "/getAccounts")
    public List<AccountDto> getAccounts(){
        return accountMapper.mapToAccountDtoList(accountDbService.findAccounts());
    }

    @PutMapping(value = "changeLogin/{oldLogin}&{newLogin}")
    public void changeLogin(@PathVariable String oldLogin, @PathVariable String newLogin){
        if (!accountDbService.findAccountByLogin(oldLogin).isPresent()) {
            throw new AccountNotFoundException();
        }
        AccountEntity accountEntity = accountDbService.findAccountByLogin(oldLogin).get();
        accountEntity.setLogin(newLogin);
        accountDbService.save(accountEntity);
    }

    private boolean checkIfEmailOrLoginIsUnique(String email, String login){
        return !accountDbService.findAccountWithTheSameLoginOrEmail(login, email).isPresent();
    }

}