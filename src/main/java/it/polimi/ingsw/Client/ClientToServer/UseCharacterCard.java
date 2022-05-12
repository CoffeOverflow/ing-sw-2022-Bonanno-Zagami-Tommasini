package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;

public class UseCharacterCard implements ClientToServerMessage{

    private String asset;
    //TODO capire che attributi aggiungere in base alla carta

    public UseCharacterCard(String asset){
        this.asset=asset;
    }

    public String getAsset() {
        return asset;
    }

    public void handleMessage(GameHandler game, ClientHandler player){
        Action action=new Action();
        //TODO settare attributi(da creare) in action per le carte personaggio
        game.getController().doAction(action);
    }
}
