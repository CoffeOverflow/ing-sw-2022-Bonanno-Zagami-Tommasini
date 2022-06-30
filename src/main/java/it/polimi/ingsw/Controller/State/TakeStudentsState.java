package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * @author Giuseppe Bonanno
 * it is the state to take the students of the cloud,
 * check if there are students on it and if for some error in the game you have too many students at the entrance
 */
public class TakeStudentsState implements GameControllerState{
    @Override
    public void turnAction(GameController gc, Action action){
        GameModel m=gc.getModel();
        EnumMap<Color,Integer> entryStudents=m.getPlayerByID(m.getCurrentPlayer()).getEntryStudents();
        int number=0;
        if(m.areStudentsOnCloud(action.getChooseCloud()))
        {
            for(Color c:Color.values())
                number+=entryStudents.get(c);
            if((m.getNumberOfStudent()-number)==m.getNumberOfStudentBag()) {
                m.getPlayerByID(m.getCurrentPlayer()).addEntryStudents(m.takeStudentsFromCloud(action.getChooseCloud()));
            }
            else
                System.out.println("You have too many students at the entrance");
        }
        else
            System.out.println("The cloud is empty");


    }
}
