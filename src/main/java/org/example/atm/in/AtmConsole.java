package org.example.atm.in;

import org.example.atm.model.Card;
import org.example.atm.out.ConsoleUI;
import org.example.atm.service.BankService;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AtmConsole {
    private BankService bankService;
    private double ATM_LIMIT = 10_000_000;
    private double DEPOSIT_LIMIT = 1_000_000;
    private final Scanner in = new Scanner(System.in);
    private boolean isAuthorized = false;
    private Card currentCard;
    private static final String CARD_NUMBER_REGEX = "\\d{4}-\\d{4}-\\d{4}-\\d{4}";
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile(CARD_NUMBER_REGEX);


    public AtmConsole(BankService bankService) {
        this.bankService = bankService;
        bankService.ensureDataFileExists();
        bankService.loadCardsFromFile();
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
                        System.out.print("Введите номер карты в формате (XXXX-XXXX-XXXX-XXXX): ");
                        String cardNumber = in.nextLine();

                        if (!CARD_NUMBER_PATTERN.matcher(cardNumber).matches()) {
                            System.out.println("Некорректный формат номера карты. Повторите попытку.");
                            continue;
                        }

                        Card foundCard = bankService.findCardByNumber(cardNumber);

                        if (foundCard != null) {
                            System.out.println("Карта с таким номером уже существует!");
                            continue;
                        }

                        System.out.print("Введите PIN-код: ");
                        int pin = in.nextInt();

                        if (pin > 9999 || pin < 1000) {
                            System.out.println("Invalid pin. Please try again.");
                            continue;
                        }

                        Card card = new Card(cardNumber, pin, 0, false, LocalDateTime.now());
                        bankService.addCard(card);
                        System.out.println("Карта успешно оформлена!");
                    }
                    case 2 -> {
                        System.out.print("Введите номер карты в формате (XXXX-XXXX-XXXX-XXXX): ");
                        String cardNumber = in.nextLine();

                        if (!CARD_NUMBER_PATTERN.matcher(cardNumber).matches()) {
                            System.out.println("Некорректный формат номера карты. Повторите попытку.");
                            continue;
                        }

                        Card card = bankService.findCardByNumber(cardNumber);

                        if (card == null) {
                            System.out.println("Карта не найдена.");
                            continue;
                        }

                        if (card.isBlocked()) {
                            System.out.println("Ваша карта заблокирована. Повторите попытку позже.");
                            continue;
                        }
                        authorization(card);
                    }
                    case 3 -> {
                        bankService.saveCardsToFile();
                        return;
                    }
                    default -> System.out.println("Неверная команда");
                }

            } catch (InputMismatchException inputMismatchException) {
                System.out.println("Неверный формат ввода команды. Введите корректные данные");
                in.next();
            }
        }
    }

    public void authorization(Card card) {

        for (int i = 0; i < 3; i++) {
            int pin;

            try {
                System.out.print("Введите PIN-код: ");
                pin = in.nextInt();
            } catch (InputMismatchException inputMismatchException) {
                System.out.println("Неверный формат pin-кода. Повторите попытку.");
                in.next();
                i--;
                continue;
            }

            if (pin > 9999 || pin < 1000) {
                System.out.println("Неверный формат pin-кода. Повторите попытку.");
                i--;
                continue;
            }

            if (card.getPinCode() == pin) {
                isAuthorized = true;
                break;
            } else {
                card.decreaseAttempts();
                if (card.getAttempts() == 0) {
                    card.blockCard();
                    System.out.println("У вас закончились попытки. Ваша карта будет заблокирована в течение 24 часов.");
                    return;
                } else {
                    System.out.println("Неверный pin-код. Повторите попытку.");
                    System.out.println("У вас осталось попыток: " + card.getAttempts());
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
                    System.out.println("Баланс данной карты составляет: " + currentCard.getBalance());
                }
                case 2 -> {
                    System.out.println("Введите сумму, которую хотите снять.");
                    double withdrawal_amount = in.nextDouble();
                    if (withdrawal_amount > currentCard.getBalance()) {
                        System.out.println("Недостаточно средств!");
                    } else if (withdrawal_amount > ATM_LIMIT) {
                        System.out.println("Превышен лимит банкомата!");
                    } else if (withdrawal_amount <= 0){
                        System.out.println("Сумма для снятия должны быть больше нуля!");
                    } else {
                        currentCard.setBalance(currentCard.getBalance() - withdrawal_amount);
                        ATM_LIMIT -= withdrawal_amount;
                        System.out.println("Снятие прошло успешно!");
                    }
                }
                case 3 -> {
                    System.out.println("Введите сумму, которую хотите пополнить.");
                    double replenishment_amount = in.nextDouble();
                    if (replenishment_amount > DEPOSIT_LIMIT || replenishment_amount > DEPOSIT_LIMIT - currentCard.getBalance()) {
                        System.out.println("Превышен лимит пополнения!");
                    } else {
                        currentCard.setBalance(currentCard.getBalance() + replenishment_amount);
                        System.out.println("Пополнение прошло успешно!");
                    }
                }
                case 4 -> {
                    bankService.saveCardsToFile();
                    isAuthorized = false;
                    return;
                }
                default -> System.out.println("Введена неверная команда!");
            }
        }
    }
}
