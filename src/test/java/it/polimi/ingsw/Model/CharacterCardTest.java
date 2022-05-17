package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardTest {
    CharacterCard[] cards=new CharacterCard[12];

    @BeforeEach
    void testCharacterCard(){
        String[] characterAssets={"innkeeper.jpg","auctioneer.jpg","postman.jpg","herbalist.jpg","centaur.jpg",
                "clown.jpg", "infantryman.jpg", "lumberjack.jpg", "storyteller.jpg","princess.jpg","thief.jpg","merchant.jpg"};
        for(int i=0; i<12;i++) {
            if(i==0 || i==9){
                EnumMap<Color, Integer> students=new EnumMap<>(Color.class);
                students.put(Color.BLUE,2);
                students.put(Color.GREEN,1);
                students.put(Color.PINK,1);
                cards[i] = new CharacterCard(characterAssets[i],students);
            }else if(i==5){
                EnumMap<Color, Integer> students=new EnumMap<>(Color.class);;
                students.put(Color.BLUE,2);
                students.put(Color.GREEN,1);
                students.put(Color.PINK,1);
                students.put(Color.RED,2);
                cards[i] = new CharacterCard(characterAssets[i],students);
            }else{
                cards[i] = new CharacterCard(characterAssets[i]);
            }
        }
    }
    @Test
    void testSetChosenStudents() {
        EnumMap<Color, Integer> chosenStudents=new EnumMap<>(Color.class);
        EnumMap<Color, Integer> chosenStudents2=new EnumMap<>(Color.class);
        EnumMap<Color, Integer> chosenStudents3=new EnumMap<>(Color.class);
        chosenStudents.put(Color.BLUE,1);
        chosenStudents2.put(Color.GREEN,1);
        cards[0].setChosenStudents(chosenStudents);
        cards[9].setChosenStudents(chosenStudents2);
        chosenStudents3.put(Color.PINK,1);
        chosenStudents3.put(Color.GREEN,1);
        cards[5].setChosenStudents(chosenStudents3);
        assertEquals(cards[0].getChosenStudents().get(),chosenStudents);
        assertEquals(cards[9].getChosenStudents().get(),chosenStudents2);
        assertEquals(cards[5].getChosenStudents().get(),chosenStudents3);
    }

    @Test
    void testUseCard() {

    }

    @Test
    void testIncreaseCost() {
    }
}