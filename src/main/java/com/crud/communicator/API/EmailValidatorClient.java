package com.crud.communicator.API;

import com.crud.communicator.domain.dto.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class EmailValidatorClient {

    @Autowired
    private RestTemplate restTemplate;

    public EmailDto validateEmail(String email){
        URI url = UriComponentsBuilder.fromHttpUrl("https://pozzad-email-validator.p.rapidapi.com/emailvalidator/validateEmail/")
                .path(email)
                .build().encode().toUri();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("X-RapidAPI-Host", "pozzad-email-validator.p.rapidapi.com");
        headers.add("X-RapidAPI-Key", "08ae5b7c8bmsh79760d9f868a764p163794jsn3d576565ac64");
        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<EmailDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, EmailDto.class);
        return response.getBody();
    }
}