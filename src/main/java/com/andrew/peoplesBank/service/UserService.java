package com.andrew.peoplesBank.service;

import com.andrew.peoplesBank.dto.AccountEnquiry;
import com.andrew.peoplesBank.dto.BankResponse;
import com.andrew.peoplesBank.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(AccountEnquiry accountEnquiry);
    String nameEnquiry(AccountEnquiry accountEnquiry);

}
