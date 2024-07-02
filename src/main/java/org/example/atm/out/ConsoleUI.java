package org.example.atm.out;

public class ConsoleUI {
    public static void printUserCommands() {
        System.out.print("""
                Выберите команду:
                1 - Оформить новую карту
                2 - Использовать существующую карту
                3 - Выход
                """);
    }

    public static void printCardCommands() {
        System.out.print("""
                Выберите команду:
                1 - Проверить баланс
                2 - Снять деньги с карты
                3 - Пополнить карту
                4 - Выход
                """);
    }
}
