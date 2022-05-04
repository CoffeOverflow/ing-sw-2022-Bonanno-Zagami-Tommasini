package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;

import static it.polimi.ingsw.Constants.ANSI_RESET;
import static it.polimi.ingsw.Constants.ANSI_YELLOW;

public class WaitForOtherPlayer implements ServerToClientMessage {
    private static String msg= ANSI_YELLOW + "Wait for other players..." + ANSI_RESET;

    public static String getMsg() {
        return msg;
    }

    @Override
    public void handle(View view) throws IOException {
        view.showMessage(msg + "\n");
    }
}
