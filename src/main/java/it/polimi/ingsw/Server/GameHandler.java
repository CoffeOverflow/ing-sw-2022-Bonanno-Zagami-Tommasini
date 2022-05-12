package it.polimi.ingsw.Server;

import it.polimi.ingsw.Server.ServerToClient.GenericMessage;
import it.polimi.ingsw.Server.ServerToClient.ServerToClientMessage;
import it.polimi.ingsw.Server.ServerToClient.WaitForOtherPlayer;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Constants.*;

public class GameHandler {
    private final int gameID;
    private final String name;
    private final int numberOfPlayers;
    private final boolean expertMode;
    private final List<ClientHandler> players;

    public GameHandler(int gameID, String name, int numberOfPlayers, boolean expertMode){
        this.gameID = gameID;
        this.name = name;
        this.numberOfPlayers = numberOfPlayers;
        this.expertMode = expertMode;
        this.players = new ArrayList<>();
    }

    public void addPlayer(ClientHandler player){
        this.players.add(player);
        player.send(new GenericMessage(ANSI_BLUE + "\nWelcome to "+this.name+"!\n" + ANSI_RESET));
        player.send(new WaitForOtherPlayer());
    }

    public int getGameID() {
        return gameID;
    }

    public void sendAll(ServerToClientMessage message){
        for(ClientHandler client: players){
            client.send(message);
        }
    }

    public void sendAllExcept(ServerToClientMessage message, ClientHandler player){
        for(ClientHandler client: players){
            if(!client.equals(player))
                client.send(message);
        }
    }

    public void sendTo(ServerToClientMessage message, ClientHandler player){
        for(ClientHandler client: players){
            if(client.equals(player))
                client.send(message);
        }
    }

    @Override
    public String toString() {
        return gameID + ". " + name + " " + players.size() + "/" + numberOfPlayers + " players " + (expertMode ? ANSI_RED + "Expert mode" + ANSI_RESET : ANSI_GREEN + "Base mode" + ANSI_RESET);
    }
}
