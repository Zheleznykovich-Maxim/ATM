package org.example.atm.service;

import org.example.atm.model.Card;
import org.example.atm.repository.CardRepository;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class BankService {
    private final CardRepository cardRepository;
    private static final String cardFileName = "cards.txt";

    public BankService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public void loadCardsFromFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(cardFileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(" ");
                if (data.length == 3) {
                    cardRepository.addCard(new Card(data[0], Integer.parseInt(data[1]), Double.parseDouble(data[2])));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading cards: " + e.getMessage());
        }
    }

    public void saveCardsToFile() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(cardFileName))) {
            for (Card card : cardRepository.getAllCards().values()) {
                bufferedWriter.write(card.getCardNumber() + " " + card.getPinCode() + " " + card.getBalance());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving cards: " + e.getMessage());
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
