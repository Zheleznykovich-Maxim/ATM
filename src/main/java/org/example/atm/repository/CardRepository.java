package org.example.atm.repository;

import org.example.atm.model.Card;

import java.util.HashMap;
import java.util.Map;

public class CardRepository {
    private final Map<String, Card> cards;

    public CardRepository() {
        cards = new HashMap<>();
    }

    public Card findCardByNumber(String cardNumber) {
        return cards.get(cardNumber);
    }

    public void addCard(Card card) {
        cards.put(card.getCardNumber(), card);
    }

    public void updateCard(Card card) {
        cards.put(card.getCardNumber(), card);
    }

    public void removeCard(String cardNumber) {
        cards.remove(cardNumber);
    }

    public Map<String, Card> getAllCards() {
        return cards;
    }

}
