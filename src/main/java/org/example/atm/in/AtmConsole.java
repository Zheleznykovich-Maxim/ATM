package org.example.atm.in;

import org.example.atm.model.Card;
import org.example.atm.out.ConsoleUI;
import org.example.atm.service.BankService;

import java.util.Scanner;

public class AtmConsole {
    private BankService bankService;
    private double ATM_LIMIT = 1000000;
    private final Scanner in = new Scanner(System.in);
    private boolean isAuthorized = false;
    private Card currentCard;
    public AtmConsole(BankService bankService, boolean isAuthorized) {
        this.bankService = bankService;
    }

    public void start() {
        while (true) {
            try {

                if (isAuthorized) {
                    runCardCommands();
                }

                ConsoleUI.printUserCommands();

                int command = in.nextInt();
                in.nextLine();
                switch (command) {
                    case 1 -> {
                        System.out.print("Enter card number (XXXX-XXXX-XXXX-XXXX): ");
                        String cardNumber = in.nextLine();
                        System.out.print("Enter PIN: ");
                        int pin = in.nextInt();
                        Card card = new Card(cardNumber, pin, 0);
                        bankService.addCard(card);
                    }
                    case 2 -> {
                        System.out.print("Enter card number (XXXX-XXXX-XXXX-XXXX): ");
                        String cardNumber = in.nextLine();
                        Card card = bankService.findCardByNumber(cardNumber);

                        if (card == null) {
                            System.out.println("Card not found");
                            continue;
                        }

                        if (card.isBlocked()) {
                            System.out.println("Your card is blocked. Please try again later.");
                            continue;
                        }

                        authorization(card);
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void authorization(Card card) {
        for (int i = 0; i < 3; i++) {
            System.out.print("Enter PIN: ");
            int pin = in.nextInt();

            if (card.getPinCode() == pin) {
                isAuthorized = true;
                break;
            } else {
                card.decreaseAttempts();
                if (card.getAttempts() == 0) {
                    card.blockCard();
                    System.out.println("Your card has been blocked due to multiple incorrect PIN entries.");
                    break;
                } else {
                    System.out.println("Incorrect PIN. Try again.");
                }
            }
        }
        System.out.println("Авторизация произошла успешно!");
        currentCard = card;
        isAuthorized = true;
    }

    public void runCardCommands() {
        while (isAuthorized) {
            ConsoleUI.printCardCommands();

            int command = in.nextInt();
            in.nextLine();

            switch (command) {
                case 1 -> {
                    System.out.print("Баланс данной карты составляет:" + currentCard.getBalance());
                }
                case 2 -> {
                    System.out.println("Введите сумму, которую хотите снять.");
                    double withdrawal_amount = in.nextDouble();
                    if (withdrawal_amount > currentCard.getBalance()) {
                        System.out.println("Недостаточно средств!");
                    } else if (withdrawal_amount > ATM_LIMIT) {
                        System.out.println("Превышен лимит банкомата!");
                    } else {
                        currentCard.setBalance(currentCard.getBalance() - withdrawal_amount);
                        System.out.println("Снятие прошло успешно!");
                    }
                }
                case 3 -> {
                    System.out.println("Введите сумму, которую хотите пополнить.");
                    double replenishment_amount = in.nextDouble();
                    if (replenishment_amount > currentCard.getBalance()) {
                        System.out.println("Превышен лимит банкомата!");
                    } else {
                        currentCard.setBalance(currentCard.getBalance() + replenishment_amount);
                        System.out.println("Пополнение прошло успешно!");
                    }
                }
                case 4 -> {
                    bankService.saveCardsToFile();
                    isAuthorized = false;
                }
                default -> System.out.println("Введена неверная команда!");
            }
        }
    }
}
