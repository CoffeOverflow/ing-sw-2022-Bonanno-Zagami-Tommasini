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
    public void turnAction(GameController gc, Action action){
        m=gc.getModel();
        Tower towerOnIsland;
        if(action.getMove().equals(MoveTo.ISLAND)){
            if(m.getTowerOnIsland(action.getPosIsland()).isPresent()){
                towerOnIsland=m.getTowerOnIsland(action.getPosIsland()).get();
                if(towerOnIsland.equals(m.getPlayerTower(m.getCurrentPlayer()))){
                    if(m.isPresentEntryPlayer(action.getColorStudent())){
                        m.moveStudentToIsland(action.getPosIsland(), action.getColorStudent());
                        m.removeEntryStudents(action.getColorStudent());
                    }
                    else
                        System.out.println("You don't have students of this color at the entrance ");
                }
                else
                    System.out.println("You haven't conquered this island");

            }
            else
                System.out.println("You haven't conquered this island");

        }
        else if(action.getMove().equals(MoveTo.SCHOOL)){
                m.moveToSchool(m.getCurrentPlayer(),action.getColorStudent());
        }
    }
}
