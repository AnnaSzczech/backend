package com.crud.communicator.controller;

import com.crud.communicator.API.EmailValidatorClient;
import com.crud.communicator.domain.dto.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/emailValidator")
public class EmailValidatorController {

    @Autowired
    private EmailValidatorClient emailValidatorClient;

    @GetMapping(value = "/{email}")
    public EmailDto validateEmail(@PathVariable String email){
        return emailValidatorClient.validateEmail(email);
    }
}
