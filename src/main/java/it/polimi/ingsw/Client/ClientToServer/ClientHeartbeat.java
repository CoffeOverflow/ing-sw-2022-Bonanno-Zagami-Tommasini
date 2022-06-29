package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;

/**
 * ClientHeartBeat class
 * implementation of a setUp message from client to server to check if the connection is still on
 * @author Angelo Zagami
 */
public class ClientHeartbeat implements ClientToServerMessage{
    @Override
    public void handleMessage(GameHandler game, ClientHandler player) {
        return;
    }
}
