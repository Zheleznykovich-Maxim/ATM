package org.example.atm.service;

import org.example.atm.model.Card;
import org.example.atm.repository.CardRepository;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class BankService {
    private final CardRepository cardRepository;
    private static final String cardFileName = "src/main/resources/cards.txt";

    public BankService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public void ensureDataFileExists() {
        File file = new File(cardFileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating data file: " + e.getMessage());
            }
        }
    }
    public void loadCardsFromFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(cardFileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(" ");
                if (data.length == 5) {
                    cardRepository.addCard(
                            new Card(
                                    data[0],
                                    Integer.parseInt(data[1]),
                                    Double.parseDouble(data[2]),
                                    Boolean.parseBoolean(data[3]),
                                    LocalDateTime.parse(data[4])
                            ));
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка в загрузке карт: " + e.getMessage());
        }
    }

    public void saveCardsToFile() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(cardFileName))) {
            for (Card card : cardRepository.getAllCards().values()) {
                bufferedWriter.write(
                          card.getCardNumber() +
                        " " + card.getPinCode() +
                        " " + card.getBalance() +
                        " " + card.isBlocked() +
                        " " + card.getBlockDateTime()
                );
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            System.out.println("Ошибка в сохранении карт: " + e.getMessage());
        }
    }

    public void addCard(Card card) {
        cardRepository.addCard(card);
    }

    public void removeCard(String cardNumber) {
        cardRepository.removeCard(cardNumber);
    }

    public Card findCardByNumber(String cardNumber) {
        return cardRepository.findCardByNumber(cardNumber);
    }

    public void updateCard(Card card) {
        cardRepository.updateCard(card);
    }

    public Map<String, Card> getAllCards() {
        return cardRepository.getAllCards();
    }
}
