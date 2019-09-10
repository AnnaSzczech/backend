package com.crud.communicator.controller;

import com.crud.communicator.domain.AccountEntity;
import com.crud.communicator.domain.UserEntity;
import com.crud.communicator.domain.dto.AccountDto;
import com.crud.communicator.domain.dto.LoginDto;
import com.crud.communicator.mapper.AccountMapper;
import com.crud.communicator.service.AccountDbService;
import com.crud.communicator.service.MessageDbService;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTestSuite {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountDbService service;

    @MockBean
    private AccountMapper accountMapper;

    @MockBean
    private MessageDbService messageDbService;


    @Test
    public void testGetLogins() throws Exception {
        //given
        List<AccountEntity> accounts = new ArrayList<>();
        AccountEntity accountEntity = new AccountEntity("login", "password");
        accounts.add(accountEntity);

        List<LoginDto> loginDtos = new ArrayList<>();
        LoginDto loginDto = new LoginDto("login", true);
        loginDtos.add(loginDto);
        when(service.findLogins()).thenReturn(accounts);
        when(accountMapper.mapToLoginDtoList(accounts)).thenReturn(loginDtos);

        //when && then
        mockMvc.perform(get("/v1/accounts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].login", is("login")))
                .andExpect(jsonPath("$[0].online", is(true)));
    }

    @Test
    public void testGetAccount() throws Exception {
        //given
        AccountEntity accountEntity = new AccountEntity("login", "password");

        AccountDto accountDto = new AccountDto(1L, "name", "surname", "email", "login", "password", true);

        when(service.findAccount("login", "password")).thenReturn(Optional.of(accountEntity));
        when(accountMapper.mapToAccountDto(accountEntity)).thenReturn(accountDto);

        //when && then
        mockMvc.perform(get("/v1/accounts/login&password").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.surname", is("surname")))
                .andExpect(jsonPath("$.email", is("email")))
                .andExpect(jsonPath("$.login", is("login")))
                .andExpect(jsonPath("$.password", is("password")))
                .andExpect(jsonPath("$.online", is(true)));
    }

    @Test
    public void testGetAccounts() throws Exception {
        //given
        List<AccountEntity> accounts = new ArrayList<>();
        AccountEntity accountEntity = new AccountEntity("login", "password");
        accounts.add(accountEntity);

        List<AccountDto> accountDtos = new ArrayList<>();
        AccountDto accountDto = new AccountDto(1L, "name", "surname", "email", "login", "password", true);
        accountDtos.add(accountDto);

        when(service.findAccounts()).thenReturn(accounts);
        when(accountMapper.mapToAccountDtoList(accounts)).thenReturn(accountDtos);

        //when && then
        mockMvc.perform(get("/v1/accounts/getAccounts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("name")))
                .andExpect(jsonPath("$[0].surname", is("surname")))
                .andExpect(jsonPath("$[0].email", is("email")))
                .andExpect(jsonPath("$[0].login", is("login")))
                .andExpect(jsonPath("$[0].password", is("password")))
                .andExpect(jsonPath("$[0].online", is(true)));
    }

    @Test
    public void testDeleteAccount() throws Exception{
        //given
        AccountEntity accountEntity = new AccountEntity("login", "password");
        when(service.findAccountByLogin("login")).thenReturn(Optional.of(accountEntity));
        doNothing().when(service).deleteAccount("login");
        doNothing().when(messageDbService).deleteMessages("login");

        //when && then
        mockMvc.perform(delete("/v1/accounts/login").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddAccount() throws Exception{
        //given
        AccountDto accountDto = new AccountDto(1L, "name", "surname", "email123", "login", "password", false);
        AccountEntity accountEntity = new AccountEntity("login", "password");
        UserEntity userEntity = new UserEntity("name", "surname", "email123");
        accountEntity.setUser(userEntity);

        when(service.save(accountEntity)).thenReturn(accountEntity);
        when(accountMapper.mapToAccount(accountDto)).thenReturn(accountEntity);
        when(service.findAccountWithTheSameLoginOrEmail("login", "email")).thenReturn(Optional.of(accountEntity));

        Gson gson = new Gson();
        String jsonContent = gson.toJson(accountDto);

        //when && then
        mockMvc.perform(post("/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogIn() throws Exception{
        //given
        AccountEntity accountEntity = new AccountEntity("login", "password");

        when(service.save(accountEntity)).thenReturn(accountEntity);
        when(service.findAccount("login", "password")).thenReturn(Optional.of(accountEntity));

        //when && then
        mockMvc.perform(put("/v1/accounts/logIn/login&password"))
                .andExpect(status().isOk());
    }
}