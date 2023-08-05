package com.andrew.peoplesBank.service;

import com.andrew.peoplesBank.dto.*;
import com.andrew.peoplesBank.entity.User;
import com.andrew.peoplesBank.repository.UserRepository;
import com.andrew.peoplesBank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    TransactionService transactionService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        /**
         * Creating amn account - saving a new user into the db
         * Check if user already has an account
         */

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .address(userRequest.getAddress())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();

        User savedUser = userRepository.save(newUser);
        // Send email Alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Creation")
                .messageBody("Congratulations! Your Account has been successfully Created\n Your Account Details " +
                "Account Name: " + savedUser.getFirstName() + " " + savedUser.getOtherName()
                        + " " + savedUser.getLastName() + "\n Your Account Number " + savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " "
                                + savedUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(AccountEnquiry accountEnquiry) {
        // Check if the provided account number exists in the database
        boolean isAccountExist = userRepository.existsByAccountNumber(accountEnquiry.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User foundUser = userRepository.findByAccountNumber(accountEnquiry.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(foundUser.getFirstName().concat(" ")
                                .concat(foundUser.getOtherName()).concat(" ")
                                 .concat(foundUser.getLastName()))
                        .accountNumber(foundUser.getAccountNumber())
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(AccountEnquiry accountEnquiry) {

        boolean isAccountExist = userRepository.existsByAccountNumber(accountEnquiry.getAccountNumber());
        if (!isAccountExist) {
            return AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE;
        }

        User foundUser = userRepository.findByAccountNumber(accountEnquiry.getAccountNumber());
        return foundUser.getFirstName().concat(" ").concat(foundUser.getOtherName().concat(" ")
                .concat(foundUser.getLastName()));
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditRequest) {
        // Check if the account exists
        boolean isAccountExist = userRepository.existsByAccountNumber(creditRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToBeCredited = userRepository.findByAccountNumber(creditRequest.getAccountNumber());
        userToBeCredited.setAccountBalance(userToBeCredited.getAccountBalance().add(creditRequest.getAmount()));
        userRepository.save(userToBeCredited);

        // Save transactions
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToBeCredited.getAccountNumber())
                .transactionType("CREDIT")
                .amount(creditRequest.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToBeCredited.getFirstName().concat(" ")
                                .concat(userToBeCredited.getOtherName()).concat(" ")
                                .concat(userToBeCredited.getLastName()))
                        .accountNumber(userToBeCredited.getAccountNumber())
                        .accountBalance(userToBeCredited.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest debitRequest) {
        // Check if the account exists
        // Check for sufficient funds
        boolean isAccountExist = userRepository.existsByAccountNumber(debitRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToBeDebited = userRepository.findByAccountNumber(debitRequest.getAccountNumber());

        if (userToBeDebited.getAccountBalance().compareTo(debitRequest.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        } else {

            userToBeDebited.setAccountBalance(userToBeDebited
                    .getAccountBalance().subtract(debitRequest.getAmount()));
            userRepository.save(userToBeDebited);

            TransactionDto transactionDto = TransactionDto.builder()
                    .accountNumber(userToBeDebited.getAccountNumber())
                    .transactionType("DEBIT")
                    .amount(debitRequest.getAmount())
                    .build();

            transactionService.saveTransaction(transactionDto);

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToBeDebited.getFirstName().concat(" ")
                                    .concat(userToBeDebited.getOtherName()).concat(" ")
                                    .concat(userToBeDebited.getLastName()))
                            .accountNumber(userToBeDebited.getAccountNumber())
                            .accountBalance(userToBeDebited.getAccountBalance())
                            .build())
                    .build();
        }
    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {
        // Get the source account (check if it exists)
        // Check for sufficient balance to enable transaction from source account
        // Debit the source account
        // Get the target account
        // Credit the target account
        boolean isSourceAccountExists =
                userRepository.existsByAccountNumber(transferRequest.getSourceAccount());
        boolean isDestinationAccountExists =
                userRepository.existsByAccountNumber(transferRequest.getDestinationAccount());
        if (!isSourceAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.DEBIT_ACCOUNT_DOES_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.DEBIT_ACCOUNT_DOES_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        if (!isDestinationAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.CREDIT_ACCOUNT_DOES_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.CREDIT_ACCOUNT_DOES_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User sourceAccount = userRepository.findByAccountNumber(transferRequest.getSourceAccount());

        if (transferRequest.getAmount().compareTo(sourceAccount.getAccountBalance()) >= 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(transferRequest.getAmount()));
        userRepository.save(sourceAccount);

        EmailDetails debitAlert = EmailDetails.builder()
                .recipient(sourceAccount.getEmail())
                .subject("Debit Alert")
                .messageBody("The sum of ".concat(transferRequest.getAmount().toString())
                        .concat(" has been debited from your account! Your current balance is ")
                        .concat(sourceAccount.getAccountBalance().toString()))
                .build();
        System.out.println(debitAlert);

        User destinationAccount = userRepository.findByAccountNumber(transferRequest.getDestinationAccount());
        destinationAccount.setAccountBalance(destinationAccount.getAccountBalance().add(transferRequest.getAmount()));
        userRepository.save(destinationAccount);

        EmailDetails creditAlert = EmailDetails.builder()
                .recipient(destinationAccount.getEmail())
                .subject("Credit Alert")
                .messageBody("The sum of ".concat(transferRequest.getAmount().toString())
                        .concat(" has been credited to your account! from ").concat(sourceAccount.getFirstName().concat(" ")
                                .concat(sourceAccount.getLastName()).concat(" Your current balance is ")
                        .concat(destinationAccount.getAccountBalance().toString())))
                .build();
        System.out.println(creditAlert);

        emailService.sendEmailAlert(debitAlert);
        emailService.sendEmailAlert(creditAlert);

        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(sourceAccount.getAccountNumber())
                .transactionType("DEBIT")
                .amount(transferRequest.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);

        TransactionDto transactionDto1 = TransactionDto.builder()
                .accountNumber(destinationAccount.getAccountNumber())
                .transactionType("CREDIT")
                .amount(transferRequest.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto1);


        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(null)
                .build();
    }

}
