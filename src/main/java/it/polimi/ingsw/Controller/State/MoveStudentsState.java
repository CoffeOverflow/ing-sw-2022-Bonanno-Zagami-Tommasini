package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Tower;

import java.util.Optional;

/**
 * @author Giuseppe Bonanno
 */
public class MoveStudentsState implements GameControllerState{

    private GameModel m;
    @Override
    public void turnAction(GameController gc, Action action)throws IllegalArgumentException{
        m=gc.getModel();
        Tower towerOnIsland;
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
                if(m.getPlayerByID(m.getCurrentPlayer()).getStudentsOf(action.getColorStudent())<10)
                    m.moveToSchool(m.getCurrentPlayer(),action.getColorStudent());
            }
            else
                throw new IllegalArgumentException("You don't have students of this color at your entrance");
        }
    }
}
