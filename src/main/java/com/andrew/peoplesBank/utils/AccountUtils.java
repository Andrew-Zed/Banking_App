package com.andrew.peoplesBank.utils;

import java.time.Year;

/**
 * Created by Andrew-Zed
 */

public class AccountUtils  {

    public static final String ACCOUNT_EXIST_CODE = "001";
    public static final String ACCOUNT_EXIST_MESSAGE = "This User already has an account Created!";
    public static final String ACCOUNT_CREATED_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "User successfully created!";
    public static final String ACCOUNT_DOES_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_DOES_NOT_EXIST_MESSAGE = "User with the provided Account Number does not exist";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_SUCCESS_MESSAGE = "User Account Found";
    public static final String ACCOUNT_CREDITED_SUCCESS_CODE = "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "Account Credited Successfully";
    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";
    public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "007";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "Account successfully debited";



    public static String generateAccountNumber() {

        /**
         * 2023 + randomSixDigits
         */

        Year currentYear = Year.now();
        int minSixDigits = 100000;
        int maxSixDigits = 999999;

        //generate a random number between minSixDigits and maxSixDigits
        int randNumber = (int) Math.floor(Math.random() * (maxSixDigits - minSixDigits + 1) + minSixDigits);
        // Convert the current year and randNumber to a strings, then concatenate

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);

        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNumber).toString();

    }


}
