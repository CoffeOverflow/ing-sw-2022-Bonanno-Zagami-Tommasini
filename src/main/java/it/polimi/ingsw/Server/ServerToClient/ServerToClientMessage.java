package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;
import java.io.Serializable;

public interface ServerToClientMessage extends Serializable {
    void handle(View view) throws IOException;

}
