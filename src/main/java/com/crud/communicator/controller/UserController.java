package com.crud.communicator.controller;

import com.crud.communicator.controller.exceptions.AccountNotFoundException;
import com.crud.communicator.domain.AccountEntity;
import com.crud.communicator.service.AccountDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private AccountDbService accountDbService;

    @PutMapping(value = "/changeEmail/{login}&{newEmail}")
    public void changeEmail(@PathVariable String login, @PathVariable String newEmail){
        if (!accountDbService.findAccountByLogin(login).isPresent()) {
            throw new AccountNotFoundException();
        }
        AccountEntity accountEntity = accountDbService.findAccountByLogin(login).get();
        accountEntity.getUser().setEmail(newEmail);
        accountDbService.save(accountEntity);
    }

    @PutMapping(value = "/changeName/{login}&{name}")
    public void changeName(@PathVariable String login, @PathVariable String name){
        if (!accountDbService.findAccountByLogin(login).isPresent()) {
            throw new AccountNotFoundException();
        }
        AccountEntity accountEntity = accountDbService.findAccountByLogin(login).get();
        accountEntity.getUser().setName(name);
        accountDbService.save(accountEntity);
    }

    @PutMapping(value = "/changeSurname/{login}&{surname}")
    public void changeSurname(@PathVariable String login, @PathVariable String surname){
        if (!accountDbService.findAccountByLogin(login).isPresent()) {
            throw new AccountNotFoundException();
        }
        AccountEntity accountEntity = accountDbService.findAccountByLogin(login).get();
        accountEntity.getUser().setSurname(surname);
        accountDbService.save(accountEntity);
    }
}
