package com.crud.communicator.controller;

import com.crud.communicator.domain.dto.LoveCalculatorDto;
import com.crud.communicator.API.LoveCalculatorClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/calculator")
public class LoveCalculatorController {

    @Autowired
    private LoveCalculatorClient loveCalculatorClient;

    @GetMapping
    public LoveCalculatorDto getPercentage(@RequestParam("fname") String fname, @RequestParam("sname") String sname){
        return loveCalculatorClient.getPercentage(fname, sname);
    }
}