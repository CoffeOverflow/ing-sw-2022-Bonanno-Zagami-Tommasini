package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.Color;

public class MoveStudent implements ClientToServerMessage{

    private MoveTo moveTo;

    private Color studentColor;

    private int islandPosition;

    public MoveStudent(MoveTo move, Color color, int position){
        moveTo=move;
        studentColor=color;
        islandPosition=position;
    }

    public MoveStudent(MoveTo move, Color color){
        moveTo=move;
        studentColor=color;
    }

    public MoveTo getMoveTo() {
        return moveTo;
    }

    public Color getStudentColor() {
        return studentColor;
    }

    public int getIslandPosition() {
        return islandPosition;
    }

    public void handleMessage(GameController controller){
        Action action=new Action();
        action.setMove(moveTo);
        action.setColorStudent(studentColor);
        action.setPosIsland(islandPosition);
        controller.doAction(action);
    }
}
