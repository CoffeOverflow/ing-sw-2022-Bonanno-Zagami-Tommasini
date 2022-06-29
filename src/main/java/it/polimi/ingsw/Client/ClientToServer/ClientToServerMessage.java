package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;
import java.io.Serializable;

/**
 * interface of a client to server message
 */
public interface ClientToServerMessage extends Serializable {

     /**
      * method that elaborates the message and do consequent actions implementing the logic of the game
      * @param game instance of the game handler
      * @param player client who sent the message
      */
     public void handleMessage(GameHandler game, ClientHandler player);
}
