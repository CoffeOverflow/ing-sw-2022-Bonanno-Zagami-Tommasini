package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.State.GameControllerState;
import it.polimi.ingsw.Controller.State.MoveStudentsState;
import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Controller.State.TakeStudentsState;
import it.polimi.ingsw.Model.Color;
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

    @BeforeEach
    public void setUp(){
        gc=new GameController(false,2);
        gc.getModel().addPlayer(1,"aaaa");
        gc.getModel().addPlayer(2,"bbbb");
        this.gc.getModel().setCurrentPlayer(1);
    }
}
