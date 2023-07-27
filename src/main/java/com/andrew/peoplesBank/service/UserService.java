package com.andrew.peoplesBank.service;

import com.andrew.peoplesBank.dto.*;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(AccountEnquiry accountEnquiry);
    String nameEnquiry(AccountEnquiry accountEnquiry);
    BankResponse creditAccount(CreditDebitRequest creditRequest);
    BankResponse debitAccount(CreditDebitRequest debitRequest);
    BankResponse transfer(TransferRequest transferRequest);
}
