package com.andrew.peoplesBank.repository;

import com.andrew.peoplesBank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {



}
