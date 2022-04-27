package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;

public class UseCharacterCard implements ClientToServerMessage{

    private String asset;
    //TODO capire che attributi aggiungere in base alla carta

    public UseCharacterCard(String asset){
        this.asset=asset;
    }

    public String getAsset() {
        return asset;
    }

    public void handleMessage(GameController controller){
        Action action=new Action();
        //TODO settare attributi(da creare) in action per le carte personaggio
        controller.doAction(action);
    }
}
