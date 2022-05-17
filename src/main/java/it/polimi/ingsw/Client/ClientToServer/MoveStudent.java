package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;

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

    public void handleMessage(GameHandler game, ClientHandler player){
        Action action=new Action();
        action.setMove(moveTo);
        action.setColorStudent(studentColor);
        action.setPosIsland(islandPosition);
        game.getController().doAction(action);
    }
}
