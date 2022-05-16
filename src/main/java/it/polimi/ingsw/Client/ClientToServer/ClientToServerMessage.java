package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;

import java.io.Serializable;

public interface ClientToServerMessage extends Serializable {

     public void handleMessage(GameHandler game, ClientHandler player);
}
