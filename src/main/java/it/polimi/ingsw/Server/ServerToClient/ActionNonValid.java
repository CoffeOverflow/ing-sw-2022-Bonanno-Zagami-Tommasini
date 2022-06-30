package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import java.io.IOException;
import static it.polimi.ingsw.Constants.ANSI_RED;
import static it.polimi.ingsw.Constants.ANSI_RESET;

/**
 * implementation of a message from server to client to inform the client of an invalid action
 * @author Federica Tommasini, Angelo Zagami
 */
public class ActionNonValid implements ServerToClientMessage{
    private static final String msg="the action is not valid \n";

    public static String getMsg() {
        return msg;
    }

    @Override
    public void handle(View view) throws IOException {
        view.showMessage(ANSI_RED + msg + ANSI_RESET);
    }
}
