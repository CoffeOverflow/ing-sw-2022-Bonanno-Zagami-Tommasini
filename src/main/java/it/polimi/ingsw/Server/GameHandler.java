package it.polimi.ingsw.Server;

import it.polimi.ingsw.Server.ServerToClient.GenericMessage;
import it.polimi.ingsw.Server.ServerToClient.WaitForOtherPlayer;

import java.util.ArrayList;
import java.util.List;

public class GameHandler {
    private int gameID;
    private String name;
    private int numberOfPlayers;
    private List<ClientHandler> players;

    public GameHandler(int gameID, String name){
        this.gameID = gameID;
        this.name = name;
        this.players = new ArrayList<ClientHandler>();
    }

    public void addPlayer(ClientHandler player){
        this.players.add(player);
        player.send(new GenericMessage("\n\nWelcome to "+this.name+"!\n"));
        player.send(new WaitForOtherPlayer());
    }

    @Override
    public String toString() {
        return gameID + ". " + name;
    }
}
