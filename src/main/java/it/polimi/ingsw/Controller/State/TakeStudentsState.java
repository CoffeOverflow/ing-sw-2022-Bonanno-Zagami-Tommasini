package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.GameModel;

import java.util.EnumMap;

public class TakeStudentsState implements GameControllerState{
    @Override
    public void turnAction(GameController gc, Action action){
        GameModel m=gc.getModel();
        EnumMap<Color,Integer> entryStudents=new EnumMap<Color, Integer>(Color.class);
        int number=0;
            if(m.areStudentsOnCloud(action.getChooseCloud()))
            {
                for(Color c:Color.values())
                    number+=entryStudents.get(c);
                if((m.getNumberOfStudent()-number)==(m.getNumberOfStudent()-m.getNumberOfStudentBag())) {
                    m.getPlayer(m.getCurrentPlayer()).addEntryStudents(m.takeStudentsFromCloud(action.getChooseCloud()));
                }
                else
                    System.out.println("Hai troppi studenti all'ingresso");
            }
            else
                System.out.println("La nuvola è vuota");
    }
}
