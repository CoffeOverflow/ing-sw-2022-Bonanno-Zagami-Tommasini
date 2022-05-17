package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;

public class MoveMotherNature implements ClientToServerMessage{
    private int steps;

    public MoveMotherNature(int steps){
        this.steps=steps;
    }

    public int getSteps() {
        return steps;
    }

    public void handleMessage(GameHandler game, ClientHandler player){
        Action action=new Action();
        action.setMotherNatureSteps(steps);
        game.getController().doAction(action);

    }
}
