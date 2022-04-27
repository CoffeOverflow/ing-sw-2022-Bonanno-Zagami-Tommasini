package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;

public class ChooseCloud implements ClientToServerMessage{

    private int cloud;

    public ChooseCloud(int cloud){
        this.cloud=cloud;
    }

    public int getCloud() {
        return cloud;
    }

    public void handleMessage(GameController controller){
        Action action=new Action();
        action.setChooseCloud(cloud);
        controller.doAction(action);
    }
}
