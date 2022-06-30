package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import java.io.IOException;

/**
 * implementation of a message from server to client to ask the client to select the settings of the match
 * @author Federica Tommasini, Angelo Zagami
 */
public class RequestSetUp implements ServerToClientMessage{

    private static String msg="Select the number of players and the mode of the match";

    public static String getMsg() {
        return msg;
    }

    @Override
    public void handle(View view) throws IOException {
        view.requestSetup();
    }
}
