package test.logic.models;
import main.logic.models.Card;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class CardTests {
    @Test
    public void resolveWinner_WinnerIsCallingCard_ReturnsOne(){
        Card callingCard = new Card("Id1", "Calling Card", 10.0, "Monster", "Normal", "Goblin");
        Card opposingCard = new Card("Id2", "Opposing Card", 5.0, "Monster", "Normal", "Dragon");

        int result = callingCard.resolveWinner(opposingCard);
        assertEquals(1, result);
    }

    @Test
    public void resolveWinner_WinnerIsOpposing_ReturnsNegativeOne(){
        Card callingCard = new Card("Id1", "Calling Card", 10.0, "Monster", "Normal", "Goblin");
        Card opposingCard = new Card("Id2", "Opposing Card", 50.0, "Monster", "Normal", "Dragon");

        int result = callingCard.resolveWinner(opposingCard);
        assertEquals(-1, result);
    }

    @Test
    public void resolveWinner_Draw_ReturnsZero(){
        Card callingCard = new Card("Id1", "Calling Card", 10.0, "Monster", "Normal", "Goblin");
        Card opposingCard = new Card("Id2", "Opposing Card", 10.0, "Monster", "Normal", "Dragon");

        int result = callingCard.resolveWinner(opposingCard);
        assertEquals(0, result);
    }

    @Test
    public void resolveSpecial_SpecialtyMatch_Returns0() {
        Card card = new Card("Id1", "Calling Card", 5.0, "Monster", "Normal", "goblin");
        Card opposingCard = new Card("Id2", "Opposing Card", 10.0, "Monster", "Normal", "dragon");

        double result = card.resolveSpecial(opposingCard);

        assertEquals(0, result, 0);
    }

    @Test
    public void testGetElementFromName() {
        Card card = new Card();
        card.setName("Water Elemental");
        assertEquals("water", card.getElementFromName());

        card.setName("Fire Mage");
        assertEquals("fire", card.getElementFromName());

        card.setName("Golem");
        assertEquals("normal", card.getElementFromName());
    }

    @Test
    public void testGetSpecialFromName() {
        Card card = new Card();
        card.setName("Goblin Warrior");
        assertEquals("goblin", card.getSpecialFromName());

        card.setName("Slayer Dragon");
        assertEquals("dragon", card.getSpecialFromName());

        card.setName("Ork Shaman");
        assertEquals("ork", card.getSpecialFromName());

        card.setName("Wobbly the Wizard");
        assertEquals("wizard", card.getSpecialFromName());
    }

    @Test
    public void testGetTypeFromName() {
        Card card = new Card();
        card.setName("Fireball Spell");
        assertEquals("spell", card.getTypeFromName());

        card.setName("Knight");
        assertEquals("monster", card.getTypeFromName());
    }

    @Test
    public void testCalculateSpell() {
        Card spellCard = new Card();
        spellCard.setCardType("spell");
        spellCard.setElement("fire");

        // Case 1: Same element, should return 1
        assertEquals(1, spellCard.calculateSpell("fire"), 0);

        // Case 2: Weakness, should return 0.5
        assertEquals(0.5, spellCard.calculateSpell("water"), 0);

        // Case 3: Normal effectiveness against other elements, should return 2
        assertEquals(2, spellCard.calculateSpell("normal"), 0);

        // Case 4: Non-spell card, should return 1
        Card nonSpellCard = new Card();
        nonSpellCard.setCardType("monster");
        assertEquals(1, nonSpellCard.calculateSpell("water"), 0);
    }
}
