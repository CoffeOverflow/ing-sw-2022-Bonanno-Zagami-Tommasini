package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {
    Cloud cloud;
    @BeforeEach
    public void testClassCreation() {
        Cloud cloud = new Cloud();
        for(Color color : Color.values()){
            assertEquals(cloud.getStudents().get(color), 0);
        }
    }

    @AfterEach
    public void tearDown() {
        cloud = null;
    }


    @Test
    public void testSetStudents(){
        Random random = new Random();
        EnumMap<Color, Integer> students = new EnumMap<Color, Integer>(Color.class);
        for (Color color : Color.values()) {
            students.put(color, random.nextInt(10));
        }
        cloud.setStudents(students);
        assertArrayEquals(cloud.getStudents().values().toArray(), students.values().toArray() );
    }

}