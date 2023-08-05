package com.andrew.peoplesBank.controller;

import com.andrew.peoplesBank.entity.Transaction;
import com.andrew.peoplesBank.service.BankStatement;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/bank-statement")
public class TransactionController {

     private BankStatement bankStatement;
     @GetMapping
     public List<Transaction> generateBankStatement(@RequestParam String accountNumber,
                                                    @RequestParam String startDate,
                                                    @RequestParam String endDate) throws DocumentException, FileNotFoundException {
         return bankStatement.generateStatement(accountNumber, startDate, endDate);
     }

}
