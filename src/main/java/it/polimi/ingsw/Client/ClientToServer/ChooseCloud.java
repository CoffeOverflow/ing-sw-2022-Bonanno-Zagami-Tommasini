package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;

public class ChooseCloud implements ClientToServerMessage{

    private int cloud;

    public ChooseCloud(int cloud){
        this.cloud=cloud;
    }

    public int getCloud() {
        return cloud;
    }

    public void handleMessage(GameHandler game, ClientHandler player){
        Action action=new Action();
        action.setChooseCloud(cloud);
        game.getController().doAction(action);
    }
}
