package test.logic.models;
import main.logic.models.Card;
import org.junit.jupiter.api.*;
import org.junit.jupiter.engine.*;

import static org.junit.jupiter.api.Assertions.*;

public class CardTests {
    @Test
    void resolveWinner_WinnerIsCallingCard_ReturnsOne(){
        Card callingCard = new Card("Id1", "Calling Card", 10.0, "Monster", "Normal", "Goblin");
        Card opposingCard = new Card("Id2", "Opposing Card", 5.0, "Monster", "Normal", "Dragon");

        int result = callingCard.resolveWinner(opposingCard);
        assertEquals(1, result);
    }

    @Test
    void resolveWinner_WinnerIsOpposing_ReturnsNegativeOne(){
        Card callingCard = new Card("Id1", "Calling Card", 10.0, "Monster", "Normal", "Goblin");
        Card opposingCard = new Card("Id2", "Opposing Card", 50.0, "Monster", "Normal", "Dragon");

        int result = callingCard.resolveWinner(opposingCard);
        assertEquals(-1, result);
    }

    @Test
    void resolveWinner_Draw_ReturnsZero(){
        Card callingCard = new Card("Id1", "Calling Card", 10.0, "Monster", "Normal", "Goblin");
        Card opposingCard = new Card("Id2", "Opposing Card", 10.0, "Monster", "Normal", "Dragon");

        int result = callingCard.resolveWinner(opposingCard);
        assertEquals(0, result);
    }

    @Test
    void resolveSpecial_SpecialtyMatch_Returns0() {
        Card card = new Card("Id1", "Calling Card", 5.0, "Monster", "Normal", "Goblin");
        Card opposingCard = new Card("Id2", "Opposing Card", 10.0, "Monster", "Normal", "Dragon");

        double result = card.resolveSpecial(opposingCard);

        assertEquals(0, result);
    }


}
