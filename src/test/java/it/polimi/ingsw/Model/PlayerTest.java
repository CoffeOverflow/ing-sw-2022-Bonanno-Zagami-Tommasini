package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player1;
    Player player2;

    @BeforeEach
    public void setUp()
    {
        player1 = new Player(0, "Test1", true, Tower.BLACK, 8);
        player2 = new Player(1, "Test2", false, Tower.WHITE, 8);
    }

    @AfterEach
    public void tearDown() {
        player1 = null;
        player2 = null;
    }

    @Test
    public void testAddEntryStudents(){
        Random random = new Random();
        EnumMap<Color, Integer> students = new EnumMap<Color, Integer>(Color.class);
        for (Color color : Color.values()) {
            students.put(color, random.nextInt(3));
        }
        player1.addEntryStudents(students);
        assertArrayEquals(player1.getEntryStudents().values().toArray(), students.values().toArray() );
    }

    @Test
    public void testAddStudentOf(){
        //Expert mode
        player1.setStudents(Color.BLUE, 0);
        player1.addStudentOf(Color.BLUE);
        player1.addStudentOf(Color.BLUE);
        assertEquals(player1.getMoney(), 1);
        player1.addStudentOf(Color.BLUE);
        assertEquals(player1.getMoney(), 2);
        assertEquals(player1.getStudentsOf(Color.BLUE), 3);

        //No expert mode
        player2.setStudents(Color.BLUE, 0);
        player2.addStudentOf(Color.BLUE);
        player2.addStudentOf(Color.BLUE);
        assertEquals(player2.getMoney(), 0);
        player2.addStudentOf(Color.BLUE);
        assertEquals(player2.getMoney(), 0);
        assertEquals(player2.getStudentsOf(Color.BLUE), 3);



    }

    @Test
    public void testBuildTower(){
        player1.buildTower(1);
        assertEquals(player1.getNumberOfTower(), 7);
    }

    @Test
    public void testUseAssistantCard(){
        player1.useAssistantCard("Turtle");
        for (AssistantCard card: player1.getAssistantCards()) {
            assertNotEquals(card.getName(), "Turtle");
        }
    }








}