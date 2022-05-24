package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.State.MoveStudentsState;
import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.ServerToClient.*;

public class MoveStudent implements ClientToServerMessage{

    private int numOfInstances;
    private MoveTo moveTo;

    private Color studentColor;

    private int islandPosition;

    public MoveStudent(MoveTo move, Color color, int position,int n){
        moveTo=move;
        studentColor=color;
        islandPosition=position;
        numOfInstances=n;
    }

    public MoveStudent(MoveTo move, Color color, int n){
        moveTo=move;
        studentColor=color;
        numOfInstances=n;
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
        if(!(game.getController().getState() instanceof MoveStudentsState)){
            game.getController().setState(new MoveStudentsState());
        }
        try{
            game.getController().doAction(action);
            BoardChange change=new BoardChange(moveTo,studentColor,islandPosition,game.getController().getModel().getCurrentPlayer());
            game.sendAll(new UpdateMessage(change));
            //game.sendAll(new UpdateMessage(change));//,game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
            if(numOfInstances==2) {
                game.sendTo(new GenericMessage(""+numOfInstances),game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
                game.sendTo(new ChooseOption(OptionType.MOVENATURE,game.isExpertMode()), game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
                //,game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
            }
        }catch(IllegalArgumentException e){
            game.sendTo(new ActionNonValid(), player);

        }

    }
}
