package main.logic.models;

import java.util.ArrayList;
import java.util.Vector;

public class Deck {
    private Vector<Card> deck;

    public Deck(Vector<Card> deck) {
        this.deck = deck;
    }

    public Vector<Card> getDeck() {
        return deck;
    }

    public void setDeck(Vector<Card> deck) {
        this.deck = deck;
    }

    public Card getCard(Card c){
        Card gotCard;
        gotCard = deck.get(deck.indexOf(c));
        return gotCard;
    }

    public void addCard(Card c){
        deck.add(c);
    }

    public boolean isEmpty(){
        return deck.isEmpty();
    }

    @Override
    public String toString() {
        return "Deck{" +
                "deck=" + deck +
                '}';
    }
}
