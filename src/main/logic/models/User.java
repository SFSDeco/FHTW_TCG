package main.logic.models;

import java.util.Comparator;
import java.util.Vector;

public class User {

    private int id;
    private String userName;
    private String password;
    private Vector<Card> cardStack = new Vector<>();
    private Deck cardDeck = new Deck(new Vector<>());
    private int currency = 20;
    private String authToken;

    public User(int id, String userName, String password, Vector<Card> cardStack, Vector<Card> cardDeck, int currency) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.cardStack = cardStack;
        this.cardDeck = new Deck(cardDeck);
        this.currency = currency;
    }

    public User(int id, String userName, String password, int currency, String authToken) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.currency = currency;
        this.authToken = authToken;
    }

    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public void addCardToStack(Card c){
        cardStack.add(c);
    }

    public void removeCard(Card c){
        cardStack.remove(c);
    }

    public void addPackage(cardPackage pack){
        for(Card c : pack.getCardPackage()){
            this.addCardToStack(c);
        }
        this.evaluateDeck();
    }

    public void evaluateDeck(){
        Deck newDeck = new Deck(new Vector<>());
        cardStack.sort((o1, o2) -> Double.compare(o2.getDamage(), o1.getDamage()));

        for(int i = 0; i < 4; i++){
            newDeck.addCard(cardStack.get(i));
        }

        cardDeck = newDeck;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setPassword(String password) {
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
