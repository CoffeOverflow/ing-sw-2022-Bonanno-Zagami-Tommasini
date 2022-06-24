package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;

public class AddMoney implements ServerToClientMessage{
    @Override
    public void handle(View view) throws IOException {
        view.getVmodel().getClientPlayer().addMoney();
    }
}
