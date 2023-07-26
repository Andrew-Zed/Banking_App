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
