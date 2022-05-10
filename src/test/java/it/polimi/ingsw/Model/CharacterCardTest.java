package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardTest {
    CharacterCard[] cards=new CharacterCard[12];

    @Test
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
    }

    @Test
    void testUseCard() {
    }

    @Test
    void testIncreaseCost() {
    }
}