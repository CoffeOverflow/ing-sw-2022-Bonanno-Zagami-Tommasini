package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;

public class AddMoney implements ServerToClientMessage{
    private int playerID;

    public AddMoney(int playerID){
        this.playerID = playerID;
    }
    @Override
    public void handle(View view) throws IOException {
        System.out.println(playerID);
        System.out.println(view.getVmodel().getPlayerByID(playerID).toString());
        view.getVmodel().getPlayerByID(playerID).addMoney();
    }
}
