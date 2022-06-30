package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.State.*;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class StateTest {

    private GameControllerState state;
    private GameController gc;
    private Action a=new Action();
    private Object IllegalArgumentException;

    @BeforeEach
    public void setUp(){
        gc=new GameController(false,3);
        gc.getModel().addPlayer(1,"player1");
        gc.getModel().addPlayer(2,"player2");
        gc.getModel().addPlayer(3,"player3");
        this.gc.getModel().setCurrentPlayer(1);
    }

    @Test
    public void DecideFirstPlayerTurnAction() {
        boolean assertBoolean=false;

        gc.addCurrentAssistantCard(1,new AssistantCard(1,1,"Turtle","turtle.png"));
        gc.addCurrentAssistantCard(2,new AssistantCard(8,4,"Cat","cat.png"));
        gc.addCurrentAssistantCard(3,new AssistantCard(8,4,"Cat","cat.png"));

        state=new DecideFirstPlayerState();
        gc.setFirstPlayer(0);
        try{
            state.turnAction(gc,new Action());
        }catch (Exception e){
            if(e instanceof IllegalArgumentException)
                assertBoolean=true;
            else
                assertBoolean=false;
        }
        assertTrue(assertBoolean);

        gc.addCurrentAssistantCard(3,new AssistantCard(5,3,"Lizard","lizard.png"));
        state=new DecideFirstPlayerState();
        gc.setFirstPlayer(0);
        state.turnAction(gc,new Action());
        Integer [] turnOrder=gc.getTurnOrder();
        assertTrue(turnOrder[0]==1);
        assertTrue(turnOrder[1]==3);
        assertTrue(turnOrder[2]==2);





    }


    @Test
    public void MoveStudentTurnAction(){
        this.gc.getModel().getIslandByPosition(1).setTower(this.gc.getModel().getPlayerTower(1));
        this.gc.getModel().getIslandByPosition(2).setTower(this.gc.getModel().getPlayerTower(1));
        EnumMap<Color,Integer> entry=new EnumMap<Color, Integer>(Color.class);
        entry.put(Color.BLUE,2);
        entry.put(Color.PINK,3);
        entry.put(Color.YELLOW,2);
        entry.put(Color.RED,0);
        entry.put(Color.GREEN,0);
        this.gc.getModel().getPlayerByID(1).setEntryStudents(entry);
        state=new MoveStudentsState();
        a.setMove(MoveTo.ISLAND);
        a.setPosIsland(1);
        a.setColorStudent(Color.PINK);
        state.turnAction(gc,a);

        a.setMove(MoveTo.ISLAND);
        a.setPosIsland(2);
        a.setColorStudent(Color.PINK);
        state.turnAction(gc,a);

        a.setMove(MoveTo.SCHOOL);
        a.setColorStudent(Color.BLUE);
        state.turnAction(gc,a);

        System.out.println("Students "+this.gc.getModel().getPlayerByID(1).getEntryStudents());
        System.out.println(gc.getModel().getPlayerByID(1).getStudents());

        TakeStudentTurnAction();
    }


    public void TakeStudentTurnAction() {
        state=new TakeStudentsState();
        this.gc.setState(state);

        a.setChooseCloud(1);
        this.gc.getModel().getStudentsFromBag();
        state.turnAction(gc,a);
        assertFalse(gc.getModel().areStudentsOnCloud(1));

    }


}
