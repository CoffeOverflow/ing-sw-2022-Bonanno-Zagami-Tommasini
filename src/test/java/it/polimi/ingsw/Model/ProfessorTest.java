package it.polimi.ingsw.Model;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ProfessorTest {
    Player player1;
    Player player2;
    Professor professor;

    @BeforeEach
    public void setUp()
    {
        player1 = new Player(0, "Test1", true, Tower.BLACK, 8);
        player2 = new Player(1, "Test2", false, Tower.WHITE, 8);
        professor = new Professor(Color.BLUE);
    }

    @AfterEach
    public void tearDown() {
        player1 = null;
        player2 = null;
        professor = null;
    }

    @Test
    public void testGoToSchool(){
        professor.goToSchool(player1);
        assertEquals(professor.getPlayer(), player1);
        assertTrue(player1.isPresentProfessor(Color.BLUE));
        professor.goToSchool(player2);
        assertEquals(professor.getPlayer(), player2);
        assertTrue(player2.isPresentProfessor(Color.BLUE));
        assertFalse(player1.isPresentProfessor(Color.BLUE));
    }
}