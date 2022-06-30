package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;
import java.io.Serializable;

/**
 * interface representing a message from server to client
 * @author Federica Tommasini
 */
public interface ServerToClientMessage extends Serializable {
    void handle(View view) throws IOException;

}
