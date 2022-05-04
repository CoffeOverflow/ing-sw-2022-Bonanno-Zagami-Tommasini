package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;

public class YourTurn implements ServerToClientMessage{

    private static String msg="it's your turn";

    public static String getMsg() {
        return msg;
    }

    @Override
    public void handle(View view) throws IOException {

    }
}
