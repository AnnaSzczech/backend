package com.crud.communicator.mapper;

import com.crud.communicator.domain.AccountEntity;
import com.crud.communicator.domain.UserEntity;
import com.crud.communicator.domain.dto.AccountDto;
import com.crud.communicator.domain.dto.LoginDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountMapper {
    public AccountEntity mapToAccount(final AccountDto accountDto){
        UserEntity user = new UserEntity(accountDto.getName(), accountDto.getSurname(), accountDto.getEmail());
        AccountEntity account = new AccountEntity(accountDto.getLogin(), accountDto.getPassword());
        account.setUser(user);
        return account;
    }

     public AccountDto mapToAccountDto(final AccountEntity account) {
        UserEntity user = account.getUser();
       return new AccountDto(account.getId(),user.getName(), user.getSurname(), user.getEmail(), account.getLogin(), account.getPassword(), account.isOnline());
    }

    public List<AccountDto> mapToAccountDtoList(final List<AccountEntity> account) {
        return account.stream()
                .map(accountEntity -> mapToAccountDto(accountEntity))
                .collect(Collectors.toList());
    }

    public LoginDto mapToLoginDto(final AccountEntity account) {
        return new LoginDto(account.getLogin(), account.isOnline());
    }

    public List<LoginDto> mapToLoginDtoList(final List<AccountEntity> accounts) {
        List<LoginDto> accountsDto = new ArrayList<>();
        accounts.forEach(accountEntity -> accountsDto.add(mapToLoginDto(accountEntity)));
        return accountsDto;
    }
}