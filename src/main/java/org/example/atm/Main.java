package org.example.atm;

import org.example.atm.in.AtmConsole;
import org.example.atm.repository.CardRepository;
import org.example.atm.service.BankService;

public class Main {
    public static void main(String[] args) {
        CardRepository cardRepository = new CardRepository();
        BankService bankService = new BankService(cardRepository);
        AtmConsole atmConsole = new AtmConsole(bankService);
        atmConsole.start();
    }
}