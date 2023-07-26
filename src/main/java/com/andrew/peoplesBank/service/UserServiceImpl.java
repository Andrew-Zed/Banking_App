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
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
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
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        // Check if the account exists
        boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToBeCredited = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        userToBeCredited.setAccountBalance(userToBeCredited.getAccountBalance().add(creditDebitRequest.getAmount()));
        userRepository.save(userToBeCredited);

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


}
