package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;

/**
 * implementation of a message from server to client to ask the client to choose a nickname
 * @author Angelo Zagami
 */
public class RequestNickname implements ServerToClientMessage{
    @Override
    public void handle(View view) throws IOException {
        view.requestNickname();
    }
}
