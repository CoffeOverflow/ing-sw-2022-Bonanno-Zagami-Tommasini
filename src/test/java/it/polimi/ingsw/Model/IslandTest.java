package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {

    Island island;

    @BeforeEach
    public void setUp()
    {
        island = new Island();
    }

    @AfterEach
    public void tearDown() {
        island=null;
    }

    @Test
    void testRemoveStudents() {
        EnumMap<Color, Integer> students =new EnumMap<>(Color.class);
        students.put(Color.PINK,2);
        students.put(Color.GREEN,3);
        students.put(Color.BLUE,1);
        island.setStudents(students);
        island.removeStudents(Color.PINK,1);
        island.removeStudents(Color.GREEN,3);
        island.removeStudents(Color.BLUE,2);
        island.removeStudents(Color.RED,1);
        assertEquals(island.getStudentsOf(Color.PINK),1);
        assertEquals(island.getStudentsOf(Color.GREEN),0);
        assertEquals(island.getStudentsOf(Color.BLUE),0);
        assertEquals(island.getStudentsOf(Color.RED),0);
    }

    @Test
    void testAddStudents() {
        island.addStudents(Color.BLUE,3);
        assertEquals(island.getStudentsOf(Color.BLUE),3);

    }
}