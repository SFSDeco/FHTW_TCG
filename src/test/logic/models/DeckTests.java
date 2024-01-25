package test.logic.models;

import main.logic.models.*;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.*;
public class DeckTests {

    @Test
    public void testGetWorst() {
        // Create cards
        Card c1 = new Card("Card1", "Card1", 30);
        Card c2 = new Card("Card2", "Card2", 20);
        Card c3 = new Card("Card3", "Card3", 40);
        Card c4 = new Card("Card4", "Card4", 15);

        // Create a deck
        Deck deck = new Deck(new Vector<>());
        deck.addCard(c1);
        deck.addCard(c2);
        deck.addCard(c3);
        deck.addCard(c4);

        // Set the worst card in deck
        deck.setWorst();

        // Get the worst card
        Card worstCard = deck.getWorst();

        // The worst card is the one with the minimum damage, which is c4
        assertEquals(c4, worstCard);
    }
}
