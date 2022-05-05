package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    private boolean expertMode=false;
    private List<Player> players=new ArrayList<>();
    private Tower tow=Tower.WHITE;
    private Tower tow2=Tower.BLACK;
    private List<AssistantCard> cardsAs=new ArrayList<AssistantCard>();
    private List<Island> islands=new ArrayList<>();
    private GameModel gm;
    private EnumMap<Color,Integer> studentOnIsland=new EnumMap<Color, Integer>(Color.class);



    @Test
    void Creation(){

        GameModel gm=new GameModel(expertMode, 2);

        assertEquals(gm.getNumberOfTowers(),8);
        assertEquals(gm.getNumberOfPlayers(),2);
        assertEquals(gm.getNumberOfStudent(),7);
        assertEquals(gm.getNumberOfStudentBag(),3);
    }
    @BeforeEach
    void setUp(){
        for(int i=0;i<12;i++) {
            Island isl=new Island();
            this.islands.add(isl);
        }

        gm=new GameModel(expertMode,2);
        gm.addPlayer(1,"aaa");
        gm.addPlayer(2,"bbb");

    }
    @Test
    void mergeAndMoveOnIslands() {
        int blueStudentisland1=gm.getIslandByPosition(1).getStudentsOf(Color.BLUE);
        int pinkStudentisland1=gm.getIslandByPosition(1).getStudentsOf(Color.PINK);
        int greenStudentisland1=gm.getIslandByPosition(1).getStudentsOf(Color.GREEN);
        for(Color c:Color.values())
            studentOnIsland.put(c,0);
        studentOnIsland.put(Color.BLUE,1);
        studentOnIsland.put(Color.PINK,2);
        studentOnIsland.put(Color.GREEN,1);
        gm.moveStudentsToIsland(1,studentOnIsland);
        gm.moveStudentsToIsland(2,studentOnIsland);
        gm.mergeIslands(1,2);
        int size=gm.getIslandSize();
        assertEquals(size,11);
        assertEquals(gm.getIslandByPosition(1).getStudentsOf(Color.BLUE),blueStudentisland1+2);
        assertEquals(gm.getIslandByPosition(1).getStudentsOf(Color.PINK),pinkStudentisland1+4);
        assertEquals(gm.getIslandByPosition(1).getStudentsOf(Color.GREEN),greenStudentisland1+2);
    }

    @Test
    void chooseCloud() {
        EnumMap<Color,Integer> students=new EnumMap<Color, Integer>(Color.class);
        EnumMap<Color,Integer> studentsOnCloud=new EnumMap<Color, Integer>(Color.class);
        studentsOnCloud.put(Color.BLUE,2);
        studentsOnCloud.put(Color.GREEN,1);
        gm.fillCloud(studentsOnCloud,1);


    }

    @Test
    void getPlayerInfluenceAndMoveToSchool() {
        EnumMap<Color,Integer> students=new EnumMap<Color, Integer>(Color.class);
        EnumMap<Color,Integer> students2=new EnumMap<Color, Integer>(Color.class);
        gm.moveToSchool(1,Color.BLUE);
        gm.moveToSchool(1,Color.BLUE);
        gm.moveToSchool(1,Color.BLUE);
        gm.moveToSchool(2,Color.PINK);
        gm.moveToSchool(2,Color.PINK);
        gm.moveToSchool(2,Color.PINK);
        students.put(Color.BLUE,2);
        students.put(Color.GREEN,0);
        students.put(Color.PINK,0);
        students.put(Color.RED,0);
        students.put(Color.YELLOW,0);
        students2.put(Color.BLUE,0);
        students2.put(Color.GREEN,0);
        students2.put(Color.PINK,4);
        students2.put(Color.RED,0);
        students2.put(Color.YELLOW,0);
        gm.moveStudentsToIsland(1,students);
        gm.moveStudentsToIsland(1,students2);
        assertEquals(gm.getPlayerByID(1),gm.getProfessors().get(Color.BLUE).getPlayer());
        assertEquals(gm.getPlayerByID(2),gm.getProfessors().get(Color.PINK).getPlayer());
        assertEquals(gm.getPlayerInfluence(1,1),gm.getIslandByPosition(1).getStudentsOf(Color.BLUE));
        assertEquals(gm.getPlayerInfluence(2,1),gm.getIslandByPosition(1).getStudentsOf(Color.PINK));
    }


    /**
     * The number of students in the bag after creating a game is equal to 130-(gm.getNumberOfStudent()*gm.getNumberOfPlayers()+10)
     * If we add 1 to these we will have null because the students will be finished from the bag
     */
    @Test
    void getStudentsFromBagIntero() {
        assertNotEquals(gm.getStudentsFromBag(130-(gm.getNumberOfStudent()*gm.getNumberOfPlayers()+10)),null);;
        assertEquals(gm.getStudentsFromBag(130-(gm.getNumberOfStudent()*gm.getNumberOfPlayers()+10)+1),null);

    }

}