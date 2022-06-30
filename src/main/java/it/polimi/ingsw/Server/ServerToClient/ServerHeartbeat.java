package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;

/**
 * implementation of a message from server to client to check if the connection is still on
 * @author Angelo Zagami
 */
public class ServerHeartbeat implements ServerToClientMessage{
    @Override
    public void handle(View view) throws IOException {
        return;
    }
}
