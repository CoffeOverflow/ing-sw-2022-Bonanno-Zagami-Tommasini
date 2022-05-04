package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;
import java.util.Scanner;

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
