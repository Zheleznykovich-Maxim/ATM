package org.example.atm.model;

import java.time.LocalDateTime;

public class Card {
    private final String cardNumber;
    private final int pinCode;
    private double balance;
    private int attempts;
    private boolean isBlocked;
    private LocalDateTime blockDateTime;

    public Card(String cardNumber, int pinCode, double balance, boolean isBlocked, LocalDateTime blockDateTime) {
        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
        this.balance = balance;
        this.attempts = 3;
        this.isBlocked = isBlocked;
        this.blockDateTime = blockDateTime;
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

    public LocalDateTime getBlockDateTime() {
        return blockDateTime;
    }

    public void resetAttempts() {
        this.attempts = 3;
    }

    public boolean isBlocked() {
        if (isBlocked) {
            LocalDateTime currentTime = LocalDateTime.now();
            if (blockDateTime != null && blockDateTime.plusHours(24).isBefore(currentTime)) {
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
        this.blockDateTime = LocalDateTime.now();
    }

    private double roundValue(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
