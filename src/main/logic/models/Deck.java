package main.logic.models;

import java.util.ArrayList;
import java.util.Vector;

public class Deck {
    private Vector<Card> deck;
    private Card worst;

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

    public Card getCardFromIndex(int i){
        Card gotCard;
        gotCard = deck.get(i);
        return gotCard;
    }

    public void setWorst(){
        worst = deck.get(0);
        for(Card c : deck){
            if(c.getDamage() < worst.getDamage()) worst = c;
        }
    }

    public int size(){
        return this.deck.size();
    }

    public void addCard(Card c){
        deck.add(c);
    }

    public void removeCard(Card c) {
        deck.remove(c);
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
