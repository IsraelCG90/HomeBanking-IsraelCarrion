package com.mindhub.homebanking.utils;

public final class AccountUtils {
    //CONSTRUCTOR
    private AccountUtils() {
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String accountNumber() {
        StringBuilder accountNumber = new StringBuilder();
        for (byte i = 0; i <= 2; i++) {
            accountNumber.append(getRandomNumber(0, 9));
        }
        return "VIN" + accountNumber;
    }
}
