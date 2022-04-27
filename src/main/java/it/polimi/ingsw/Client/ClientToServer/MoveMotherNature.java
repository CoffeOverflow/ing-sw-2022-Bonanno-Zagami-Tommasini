package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;

public class MoveMotherNature implements ClientToServerMessage{
    private int steps;

    public MoveMotherNature(int steps){
        this.steps=steps;
    }

    public int getSteps() {
        return steps;
    }

    public void handleMessage(GameController controller){
        Action action=new Action();
        action.setMotherNatureSteps(steps);
        controller.doAction(action);

    }
}
