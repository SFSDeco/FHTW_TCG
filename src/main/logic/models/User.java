package main.logic.models;

import java.util.Vector;

public class User {
    private String userName;
    private String password;
    private Vector<Card> cardStack = new Vector<Card>();
    private Deck cardDeck;
    private int currency = 20;

    public User(String userName, String password, Vector<Card> cardStack, Vector<Card> cardDeck, int currency) {
        this.userName = userName;
        this.password = password;
        this.cardStack = cardStack;
        this.cardDeck = new Deck(cardDeck);
        this.currency = currency;
    }

    public User(String userName, String password, Vector<Card> cardStack, Vector<Card> cardDeck) {
        this.userName = userName;
        this.password = password;
        this.cardStack = cardStack;
        this.cardDeck = new Deck(cardDeck);
    }

    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public User(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Vector<Card> getCardStack() {
        return cardStack;
    }

    public void setCardStack(Vector<Card> cardStack) {
        this.cardStack = cardStack;
    }

    public Deck getCardDeck() {
        return cardDeck;
    }

    public void setCardDeck(Deck cardDeck) {
        this.cardDeck = cardDeck;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", cardStack=" + cardStack +
                ", cardDeck=" + cardDeck +
                ", currency=" + currency +
                '}';
    }
}
