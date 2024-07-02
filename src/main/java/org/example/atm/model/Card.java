package org.example.atm.model;

public class Card {
    private final String cardNumber;
    private final int pinCode;
    private double balance;
    private int attempts;
    private boolean isBlocked;
    private long blockTime;

    public Card(String cardNumber, int pinCode, double balance) {
        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
        this.balance = balance;
        this.attempts = 3;
        this.isBlocked = false;
        this.blockTime = 0;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getPinCode() {
        return pinCode;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = roundValue(balance);
    }

    public int getAttempts() {
        return attempts;
    }

    public long getBlockTime() {
        return blockTime;
    }

    public void resetAttempts() {
        this.attempts = 3;
    }

    public boolean isBlocked() {
        if (isBlocked) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - blockTime) >= 86400000) { // 24 hours
                isBlocked = false;
                resetAttempts();
            }
        }
        return isBlocked;
    }

    public void decreaseAttempts() {
        this.attempts--;
    }

    public void blockCard() {
        this.isBlocked = true;
        this.blockTime = System.currentTimeMillis();
    }

    private double roundValue(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
