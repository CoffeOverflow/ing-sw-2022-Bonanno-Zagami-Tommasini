package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;

public class ClientHeartbeat implements ClientToServerMessage{
    @Override
    public void handleMessage(GameHandler game, ClientHandler player) {
        return;
    }
}
