package it.polimi.ingsw.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssistantCardTest {

    @Test
    public void testClassCreation() {
        AssistantCard card = new AssistantCard(2, 5, "Lion", "lion.png");
        assertEquals(card.getValue(), 2);
        assertEquals(card.getMothernaturesteps(), 5);
        assertEquals(card.getName(), "Lion");
        assertEquals(card.getAsset(), "lion.png");

    }

}