package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import java.io.IOException;

/***
 * Message sent to the client to be sure that is connected
 * @author Angelo Zagami
 */
public class ServerHeartbeat implements ServerToClientMessage{
    @Override
    public void handle(View view) throws IOException {
        return;
    }
}
