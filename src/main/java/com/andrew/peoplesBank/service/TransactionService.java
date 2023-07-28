package com.andrew.peoplesBank.service;

import com.andrew.peoplesBank.dto.TransactionDto;
import com.andrew.peoplesBank.entity.Transaction;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
