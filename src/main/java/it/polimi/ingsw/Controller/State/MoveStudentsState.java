package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Tower;

import java.util.Optional;

/**
 * @author Giuseppe Bonanno
 * The state of the controller that checks the movement of students on the islands or on the school
 */
public class MoveStudentsState implements GameControllerState{

    private GameModel m;
    @Override
    public void turnAction(GameController gc, Action action)throws IllegalArgumentException{
        m=gc.getModel();
        if(action.getMove().equals(MoveTo.ISLAND)){

                    if(m.isPresentEntryPlayer(action.getColorStudent())){
                        m.moveStudentToIsland(action.getPosIsland(), action.getColorStudent());
                        m.removeEntryStudents(action.getColorStudent());
                    }
                    else {
                        throw new IllegalArgumentException("You don't have students of this color at your entrance");
                    }

        }
        else if(action.getMove().equals(MoveTo.SCHOOL)){
            if(m.getPlayerByID(m.getCurrentPlayer()).studentIsPresent(action.getColorStudent()))
            {
                /**
                 * For consistency with the board of the game, the maximum number of students on the school for each color has been set to 9
                 */
                if(m.getPlayerByID(m.getCurrentPlayer()).getStudentsOf(action.getColorStudent())<9)
                    m.moveToSchool(m.getCurrentPlayer(),action.getColorStudent());
                else
                    throw new IllegalArgumentException("You have the maximum number of students of this color in your school");
            }
            else
                throw new IllegalArgumentException("You don't have students of this color at your entrance");
        }
    }
}
