package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;

public class WaitForOtherPlayer implements ServerToClientMessage {
    private static String msg="Wait for other players...";

    public static String getMsg() {
        return msg;
    }

    @Override
    public void handle(View view) throws IOException {
        view.printMessage(msg);
    }
}
