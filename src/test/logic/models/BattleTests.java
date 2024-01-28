package test.logic.models;

import main.logic.models.Battle;
import main.logic.models.Card;
import main.logic.models.Deck;
import main.logic.models.User;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.*;

public class BattleTests {

    /*
    #############################
        Battle Testing here
    #############################
     */
    @Test
    public void testBattleWithNoRounds() {
        User playerA = new User(1, "PlayerA", "passwordA", 100, "tokenA");
        User playerB = new User(2, "PlayerB", "passwordB", 100, "tokenB");

        Battle battle = new Battle(playerA, playerB, 0);
        battle.start();

        assertNull(battle.determineWinner(playerA, playerB));
    }

    @Test
    public void testBattleWithRounds() {
        // Create players with decks
        User playerA = new User("PlayerA", "passwordA");
        User playerB = new User("PlayerB", "passwordB");

        Deck deckA = new Deck(new Vector<>());
        deckA.addCard(new Card("CardA1", "CardA1", 10));
        deckA.addCard(new Card("CardA2", "CardA2", 20));
        deckA.addCard(new Card("CardA3", "CardA3", 30));
        deckA.addCard(new Card("CardA4", "CardA4", 40));

        Deck deckB = new Deck(new Vector<>());
        deckB.addCard(new Card("CardB1", "CardB1", 1));
        deckB.addCard(new Card("CardB2", "CardB2", 2));
        deckB.addCard(new Card("CardB3", "CardB3", 3));
        deckB.addCard(new Card("CardB4", "CardB4", 20));

        playerA.setCardDeck(deckA);
        playerB.setCardDeck(deckB);

        Battle battle = new Battle(playerA, playerB);
        battle.start();

        assertNotNull(battle.determineWinner(playerA, playerB));

    }

    @Test
    public void testBattleWithEmptyDecks() {
        // Create players with empty decks
        User playerA = new User("PlayerA", "passwordA");
        User playerB = new User("PlayerB", "passwordB");

        Battle battle = new Battle(playerA, playerB, 5);
        battle.start();

        assertNull(battle.determineWinner(playerA, playerB));
    }

    @Test
    public void testBattleWithSameDeckStrength() {
        // Create players with similar deck strength
        User playerA = new User("PlayerA", "passwordA");
        User playerB = new User("PlayerB", "passwordB");

        Deck deckA = new Deck(new Vector<>());
        deckA.addCard(new Card("CardA1", "CardA1", 20));
        deckA.addCard(new Card("CardA2", "CardA2", 30));
        deckA.addCard(new Card("CardA3", "CardA3", 40));
        deckA.addCard(new Card("CardA4", "CardA4", 50));

        Deck deckB = new Deck(new Vector<>());
        deckB.addCard(new Card("CardB1", "CardB1", 20));
        deckB.addCard(new Card("CardB2", "CardB2", 30));
        deckB.addCard(new Card("CardB3", "CardB3", 40));
        deckB.addCard(new Card("CardB4", "CardB4", 50));

        playerA.setCardDeck(deckA);
        playerB.setCardDeck(deckB);

        Battle battle = new Battle(playerA, playerB);
        battle.start();

        assertNull(battle.determineWinner(playerA, playerB));
    }

    @Test
    public void testBattleWithOnePlayerEmptyDeck() {
        // Create a scenario where one player has an empty deck
        User playerA = new User("PlayerA", "passwordA");
        User playerB = new User("PlayerB", "passwordB");

        Deck deckA = new Deck(new Vector<>()); // PlayerA has an empty deck
        Deck deckB = new Deck(new Vector<>());
        deckB.addCard(new Card("CardB1", "CardB1", 20));
        deckB.addCard(new Card("CardB2", "CardB2", 30));
        deckB.addCard(new Card("CardB3", "CardB3", 40));
        deckB.addCard(new Card("CardB4", "CardB4", 50));

        playerA.setCardDeck(deckA);
        playerB.setCardDeck(deckB);

        Battle battle = new Battle(playerA, playerB, 5);
        battle.start();

        assertNotNull(battle.determineWinner(playerB, playerA));
    }

    @Test
    public void testBattleWithMaxGamesExceeded() {
        // Create a scenario where max games are reached, but no winner yet
        User playerA = new User("PlayerA", "passwordA");
        User playerB = new User("PlayerB", "passwordB");

        Deck deckA = new Deck(new Vector<>());
        deckA.addCard(new Card("CardA1", "CardA1", 20));
        deckA.addCard(new Card("CardA2", "CardA2", 30));
        deckA.addCard(new Card("CardA3", "CardA3", 40));
        deckA.addCard(new Card("CardA4", "CardA4", 50));

        Deck deckB = new Deck(new Vector<>());
        deckB.addCard(new Card("CardB1", "CardB1", 20));
        deckB.addCard(new Card("CardB2", "CardB2", 30));
        deckB.addCard(new Card("CardB3", "CardB3", 40));
        deckB.addCard(new Card("CardB4", "CardB4", 50));

        playerA.setCardDeck(deckA);
        playerB.setCardDeck(deckB);

        Battle battle = new Battle(playerA, playerB, 3); // Set max games to 3
        battle.start();

        assertNull(battle.determineWinner(playerA, playerB));
    }

    /*
    ###############################
        resolveRound testing
    ###############################
     */
    @Test
    public void testResolveRoundPlayerAWins() {
        User playerA = new User("PlayerA", "passwordA");
        User playerB = new User("PlayerB", "passwordB");

        Deck deckA = new Deck(new Vector<>());
        deckA.addCard(new Card("CardA1", "CardA1", 20));
        playerA.setCardDeck(deckA);

        Deck deckB = new Deck(new Vector<>());
        deckB.addCard(new Card("CardB1", "CardB1", 15));
        playerB.setCardDeck(deckB);

        Battle battle = new Battle(playerA, playerB, 1);
        battle.resolveRound(playerA, playerB);

        // Player A's deck should now contain CardB1, and Player B's deck should be empty
        assertEquals(2, playerA.getCardDeck().size());
        assertEquals(0, playerB.getCardDeck().size());
    }

    @Test
    public void testResolveRoundPlayerBWins() {
        User playerA = new User("PlayerA", "passwordA");
        User playerB = new User("PlayerB", "passwordB");

        Deck deckA = new Deck(new Vector<>());
        deckA.addCard(new Card("CardA1", "CardA1", 15));
        playerA.setCardDeck(deckA);

        Deck deckB = new Deck(new Vector<>());
        deckB.addCard(new Card("CardB1", "CardB1", 20));
        playerB.setCardDeck(deckB);

        Battle battle = new Battle(playerA, playerB, 1);
        battle.resolveRound(playerA, playerB);

        // Player B's deck should now contain CardA1, and Player A's deck should be empty
        assertEquals(2, playerB.getCardDeck().size());
        assertEquals(0, playerA.getCardDeck().size());
    }

    @Test
    public void testResolveRoundDraw() {
        User playerA = new User("PlayerA", "passwordA");
        User playerB = new User("PlayerB", "passwordB");

        Deck deckA = new Deck(new Vector<>());
        deckA.addCard(new Card("CardA1", "CardA1", 20));
        playerA.setCardDeck(deckA);

        Deck deckB = new Deck(new Vector<>());
        deckB.addCard(new Card("CardB1", "CardB1", 20));
        playerB.setCardDeck(deckB);

        Battle battle = new Battle(playerA, playerB, 1);
        battle.resolveRound(playerA, playerB);

        // Both players' decks should remain unchanged
        assertEquals(1, playerA.getCardDeck().size());
        assertEquals(1, playerB.getCardDeck().size());
    }

    /*
    ###########################
            scoreCalc testing
    ###########################
     */
    @Test
    public void testScoreCalcEqualScores() {
        Battle battle = new Battle(new User("a", "a"), new User("a", "b"));

        int scoreSelf = 100;
        int scoreOpponent = 100;

        int result = battle.scoreCalc(true, scoreSelf, scoreOpponent);

        // For equal scores, the result should be the minimum value (5 in this case)
        assertEquals(110, result);
    }

    @Test
    public void testScoreCalcLowerScoreSelf() {
        Battle battle = new Battle(new User("a", "a"), new User("a", "b"));

        int scoreSelf = 90;
        int scoreOpponent = 110;

        int result = battle.scoreCalc(false, scoreSelf, scoreOpponent);

        // Since the player won and the scores are not equal, the result should be scoreSelf + change
        assertEquals(scoreSelf - 8, result);
    }

    @Test
    public void testScoreCalcLowerScoreOpponent() {
        Battle battle = new Battle(new User("a", "a"), new User("a", "b"));
        int scoreSelf = 110;
        int scoreOpponent = 90;

        int result = battle.scoreCalc(false, scoreSelf, scoreOpponent);

        // Since the player lost and the scores are not equal, the result should be scoreSelf - change
        assertEquals(scoreSelf - 12, result);
    }




}
