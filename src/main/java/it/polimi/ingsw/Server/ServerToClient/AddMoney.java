package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;

/**
 * implementation of a message from server to client to inform the client of the increasing of player's money
 * @author Angelo Zagami
 */
public class AddMoney implements ServerToClientMessage{
    private final int playerID;

    public AddMoney(int playerID){
        this.playerID = playerID;
    }
    @Override
    public void handle(View view) throws IOException {
        view.getVmodel().getPlayerByID(playerID).addMoney();
    }
}
