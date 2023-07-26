package com.andrew.peoplesBank.controller;

import com.andrew.peoplesBank.dto.AccountEnquiry;
import com.andrew.peoplesBank.dto.BankResponse;
import com.andrew.peoplesBank.dto.UserRequest;
import com.andrew.peoplesBank.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserServiceImpl userService;
    @PostMapping("/create-account")
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @GetMapping("/balance-enquiry")
    public BankResponse balanceEnquiry(@RequestBody AccountEnquiry accountEnquiry) {
        return userService.balanceEnquiry(accountEnquiry);
    }

    @GetMapping("/name-enquiry")
    public String nameEnquiry(@RequestBody AccountEnquiry accountEnquiry) {
        return userService.nameEnquiry(accountEnquiry);
    }

}
