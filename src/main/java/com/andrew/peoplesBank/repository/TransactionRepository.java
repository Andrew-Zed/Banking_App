package com.andrew.peoplesBank.repository;

import com.andrew.peoplesBank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
//    List<Transaction> findTransactionsByAccountNumberBetweenCreatedAtAndModifiedAt(String accountNumber,
//                                                                                   LocalDateTime createdAt,
//                                                                                   LocalDateTime modifiedAt);
}
