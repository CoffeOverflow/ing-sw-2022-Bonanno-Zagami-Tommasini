package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import java.io.IOException;

/**
 * implementation of a message from server to client to send a generic text message to the client
 * @author Angelo Zagami
 */
public class GenericMessage implements ServerToClientMessage{
    private final String message;

    public GenericMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void handle(View view) throws IOException {
        view.showMessage(message + "\n");
    }
}
