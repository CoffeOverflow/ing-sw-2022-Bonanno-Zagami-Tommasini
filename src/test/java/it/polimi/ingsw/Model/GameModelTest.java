package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    private List<Player> players=new ArrayList<>();
    private Tower tow=Tower.WHITE;
    private Tower tow2=Tower.BLACK;
    private List<AssistantCard> cardsAs=new ArrayList<AssistantCard>();
    private List<Island> islands=new ArrayList<>();
    private GameModel gm;



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
    @BeforeEach
    void setUp(){
        Player play=new Player(1,"aaa",true,tow,8);
        Player play2=new Player(2,"bbb",true,tow2,8);
        players.add(play);
        players.add(play2);
        for(int i=0;i<12;i++) {
            Island isl=new Island();
            this.islands.add(isl);
        }
        gm=new GameModel(true,2);
    }
    @Test
    void mergeIslands() {
        gm.mergeIslands(1,2);
        int size=gm.getIslandSize();
        assertEquals(size,11);
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