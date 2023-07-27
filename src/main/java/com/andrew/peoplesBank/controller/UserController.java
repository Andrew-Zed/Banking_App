package com.andrew.peoplesBank.controller;

import com.andrew.peoplesBank.dto.*;
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
    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest creditRequest) {
        return userService.creditAccount(creditRequest);
    }

    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest debitRequest) {
        return userService.debitAccount(debitRequest);
    }

    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest transferRequest) {
        return userService.transfer(transferRequest);
    }

}
