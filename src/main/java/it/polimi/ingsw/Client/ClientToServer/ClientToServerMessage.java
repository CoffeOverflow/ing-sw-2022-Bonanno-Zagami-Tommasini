package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.GameController;

import java.io.Serializable;

public interface ClientToServerMessage extends Serializable {

     public void handleMessage(GameController controller);
}
