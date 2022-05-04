package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.ClientToServer.ChooseNickname;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Server.Server;

import java.io.IOException;
import java.util.Scanner;

public class RequestNickname implements ServerToClientMessage{
    @Override
    public void handle(View view) throws IOException {
        view.requestNickname();
    }
}
