package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.ClientToServer.ChooseWizard;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Exceptions.MatchFullException;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Wizards;
import it.polimi.ingsw.Server.ServerToClient.*;
import it.polimi.ingsw.Server.ServerToClient.Error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static it.polimi.ingsw.Constants.*;

public class GameHandler implements Runnable{
    private final int gameID;
    private final String name;
    private final int numberOfPlayers;
    private final boolean expertMode;
    private final List<ClientHandler> players;
    private final Server server;
    private final GameController controller;
    private ArrayList<Wizards> wizards = new ArrayList<>(List.of(Wizards.values()));
    private int ready = 0;

    public GameHandler(int gameID, String name, int numberOfPlayers, boolean expertMode, Server server){
        this.gameID = gameID;
        this.name = name;
        this.numberOfPlayers = numberOfPlayers;
        this.expertMode = expertMode;
        this.players = new ArrayList<>();
        this.server = server;
        this.controller = new GameController(expertMode, numberOfPlayers);
    }

    public GameController getController() {
        return controller;
    }

    public synchronized void addPlayer(ClientHandler player) throws MatchFullException {
        if(players.size() >= numberOfPlayers)
            throw new MatchFullException();
        player.send(new GenericMessage(ANSI_BLUE + "\nWelcome to "+this.name+"!\n" + ANSI_RESET));
        this.players.add(player);
        this.controller.getModel().addPlayer(player.getPlayerID(), player.getNickname());
        player.setGame(this);
        if(players.size() == numberOfPlayers){
            server.removeAvailableGame(gameID);
            Thread gameThread = new Thread(this);
            gameThread.start();
        }
        else{
            player.send(new WaitForOtherPlayer());
        }

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

    public synchronized void playerChooseWizard(Wizards wizard, ClientHandler player){
        if(wizards.contains(wizard)){
            wizards.remove(wizard);
            controller.getModel().getPlayerByID(player.getPlayerID()).setWizard(wizard);
            ready++;
            player.send(new ActionValid());
        }
        else{
            player.send(new Error(ErrorsType.CHOSENOTVALID, "Scelta non valida"));
            player.send(new SelectWizard(wizards));
        }
    }

    public void setup(){
        sendAll(new GenericMessage("The game is starting..."));
        sendAll(new SelectWizard(wizards));
        while(ready < numberOfPlayers){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        sendAll(new GenericMessage("Match is starting..."));
        HashMap<Integer, Color> mapStudentIsland=new HashMap<>();
        for(int i=0; i<12; i++){
            for(Color c: controller.getModel().getIslandByPosition(i).getStudents().keySet()){
                if(controller.getModel().getIslandByPosition(i).getStudents().get(c)!=0)
                    mapStudentIsland.put(i,c);
            }
        }
        sendAll(new MatchCreated(controller.getModel().getMotherNaturePosition(),mapStudentIsland) );

        while(true){

        }
    }

    @Override
    public void run() {
        setup();
    }

    @Override
    public String toString() {
        return gameID + ". " + name + " " + players.size() + "/" + numberOfPlayers + " players " + (expertMode ? ANSI_RED + "Expert mode" + ANSI_RESET : ANSI_GREEN + "Base mode" + ANSI_RESET);
    }
}
