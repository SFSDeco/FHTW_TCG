package test.logic.models;

import main.logic.models.*;
import org.junit.Test;

import java.util.Comparator;
import java.util.Vector;

import static org.junit.Assert.*;

public class UserTests {
    @Test
    public void testEvaluateDeck() {
        User user = new User("TestUser", "password");

        Card c1 = new Card("Card1", "Card1", 30);
        Card c2 = new Card("Card2", "Card2", 20);
        Card c3 = new Card("Card5", "Card5", 2);
        Card c4 = new Card("Card6", "Card6", 3);
        Card c5 = new Card("Card3", "Card3", 40);
        Card c6 = new Card("Card7", "Card7", 5);
        Card c7 = new Card("Card4", "Card4", 25);

        Vector<Card> cardStack = new Vector<>();
        cardStack.add(c1);
        cardStack.add(c2);
        cardStack.add(c3);
        cardStack.add(c4);
        cardStack.add(c5);
        cardStack.add(c6);
        cardStack.add(c7);

        user.setCardStack(cardStack);

        user.evaluateDeck();

        // After evaluating the deck, the user's cardDeck should contain the top 4 cards from the cardStack
        Vector<Card> expectedDeck = new Vector<>();
        expectedDeck.add(c1);
        expectedDeck.add(c2);
        expectedDeck.add(c5);
        expectedDeck.add(c7);

        Vector<Card> userDeck = user.getCardDeck().getDeck();

        expectedDeck.sort(Comparator.comparing(Card::getDamage));
        userDeck.sort(Comparator.comparing(Card::getDamage));

        assertEquals(expectedDeck, userDeck);
    }

    @Test
    public void testAddPackage() {
        User user = new User("TestUser", "password");

        //Create cards and fill User stack
        Card c1 = new Card("Card1", "Card1", 30);
        Card c2 = new Card("Card2", "Card2", 20);
        Card c3 = new Card("Card3", "Card3", 40);
        Card c4 = new Card("Card4", "Card4", 15);

        Vector<Card> cardStack = new Vector<>();
        cardStack.add(c1);
        cardStack.add(c2);
        cardStack.add(c3);
        cardStack.add(c4);

        user.setCardStack(cardStack);

        user.evaluateDeck();

        //Create cards and fill a package
        Card c5 = new Card("Card5", "Card5", 35);
        Card c6 = new Card("Card6", "Card6", 22);
        Card c7 = new Card("Card7", "Card7", 11);
        Card c8 = new Card("Card8", "Card8", 8);
        Card c9 = new Card("Card9", "Card9", 5);

        cardPackage cardPackage = new cardPackage();
        cardPackage.addCard(c5);
        cardPackage.addCard(c6);
        cardPackage.addCard(c7);
        cardPackage.addCard(c8);
        cardPackage.addCard(c9);

        // Add the package to the user
        user.addPackage(cardPackage);

        // After adding the package, the user's cardDeck should contain the top 4 cards from the sorted cardStack
        // meaning the deck changed to the top 2 existing cards as well as the top 2 cards of the added package
        Vector<Card> expectedDeck = new Vector<>();
        expectedDeck.add(c3);
        expectedDeck.add(c1);
        expectedDeck.add(c5);
        expectedDeck.add(c6);

        Vector<Card> userDeck = user.getCardDeck().getDeck();

        expectedDeck.sort(Comparator.comparing(Card::getDamage));
        userDeck.sort(Comparator.comparing(Card::getDamage));

        assertEquals(expectedDeck, userDeck);
    }
}
