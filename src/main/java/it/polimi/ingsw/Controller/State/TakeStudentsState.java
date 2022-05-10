package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.GameModel;

import java.util.EnumMap;

/**
 * @author Giuseppe Bonanno
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
                    System.out.println("Hai troppi studenti all'ingresso");
            }
            else
                System.out.println("La nuvola è vuota");
    }
}
