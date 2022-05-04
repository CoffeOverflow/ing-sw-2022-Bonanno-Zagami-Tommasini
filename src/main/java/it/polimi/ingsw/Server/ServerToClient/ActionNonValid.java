package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;

public class ActionNonValid implements ServerToClientMessage{
    private static String msg="the action is not valid";

    public static String getMsg() {
        return msg;
    }

    @Override
    public void handle(View view) throws IOException {

    }
}
