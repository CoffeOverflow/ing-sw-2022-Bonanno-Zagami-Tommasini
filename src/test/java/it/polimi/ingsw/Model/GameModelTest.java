package it.polimi.ingsw.Model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    private List<Player> players=new ArrayList<>();
    private Tower tow=Tower.WHITE;
    private Tower tow2=Tower.BLACK;
    private List<AssistantCard> cardsAs=new ArrayList<AssistantCard>();


    @Test
    void Creation(){
        Player play=new Player(0, "aaa",true,tow,8);
        Player play2=new Player(1, "bbb",true,tow2,8);
        players.add(play);
        players.add(play2);
        GameModel gm=new GameModel(true, players);
        assertEquals(gm.getNumberOfTowers(),8);
        assertEquals(gm.getNumberOfPlayers(),2);
        assertEquals(gm.getNumberOfStudent(),7);
        assertEquals(gm.getNumberOfStudentBag(),3);
    }
    @Test
    void mergeIslands() {
        // mi sa che devo mettere i getter per poter effettuare i test in caso metterli protected??
    }

    @Test
    void moveStudentsToIsland() {
    }

    @Test
    void useCharacterCard() {
    }

    @Test
    void useAssistantCard() {
    }

    @Test
    void chooseCloud() {
    }

    @Test
    void getPlayerInfluence() {
    }

    @Test
    void moveToSchool() {
    }

    @Test
    void getStudentsFromBag() {
    }

    @Test
    void moveMotherNature() {
    }

    @Test
    void getMotherNaturePosition() {
    }

    @Test
    void setTowerOnIsland() {
    }
}