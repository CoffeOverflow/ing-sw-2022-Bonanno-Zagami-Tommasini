package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.ClientToServer.ChooseWizard;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Controller.State.DecideFirstPlayerState;
import it.polimi.ingsw.Controller.State.GameControllerState;
import it.polimi.ingsw.Exceptions.MatchFullException;
import it.polimi.ingsw.Model.*;
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

    public boolean isExpertMode() {

        return expertMode;
    }

    private final boolean             expertMode;
    private final List<ClientHandler> players;

    private int currentPlayerPosition;
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
            server.addActiveGame(this);
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

    public synchronized void sendAll(ServerToClientMessage message){
        for(ClientHandler client: players){
            client.send(message);
        }
    }

    public synchronized void sendAllExcept(ServerToClientMessage message, ClientHandler player){
        for(ClientHandler client: players){
            if(!client.equals(player))
                client.send(message);
        }
    }

    public synchronized void sendTo(ServerToClientMessage message, ClientHandler player){
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
        sendAll(new GameIsStarting("The game is starting..."));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
            boolean check=false;
            for(Color c: Color.values()) {
                if (controller.getModel().getIslandByPosition(i).getStudents().get(c) != 0){
                    mapStudentIsland.put(i, c);
                    check = true;
                }
            }
            if(check==false)
                mapStudentIsland.put(i,null);
        }
        sendAll(new MatchCreated(controller.getModel().getMotherNaturePosition(),mapStudentIsland) );
        HashMap<Integer, Wizards> mapPlayerWizard=new HashMap<>();
        HashMap<Integer, Tower> mapTowerToPlayer=new HashMap<>();
        HashMap<Integer,String> mapIDNickname=new HashMap<>();
        for(Player p: controller.getModel().getPlayers()){
            mapPlayerWizard.put(p.getPlayerID(),p.getWizard());
            mapTowerToPlayer.put(p.getPlayerID(),p.getTower());
            mapIDNickname.put(p.getPlayerID(),p.getNickname());
        }
        for(int i=0; i<players.size(); i++) {
            sendTo(new PlayersInfo(expertMode, controller.getModel().getNumberOfTowers(), mapPlayerWizard, mapTowerToPlayer, mapIDNickname, players.get(i).getPlayerID()), players.get(i));
        }
        for(Player p: controller.getModel().getPlayers()){
            sendAll(new SetUpSchoolStudent(p.getEntryStudents(),p.getPlayerID()));
        }
        if(expertMode){
            String[] characterCards= new String[3];
            HashMap<String,Integer> mapCostCard=new HashMap<>();
            for(int i=0; i<3; i++){
                characterCards[i]=controller.getModel().getCharacterCards().get(i).getAsset();
                mapCostCard.put(characterCards[i], controller.getModel().getCharacterCards().get(i).getCost());
            }
            SetUpCharacterCard msg=new SetUpCharacterCard(characterCards,mapCostCard);
            for(int i=0; i<3; i++){
                if(characterCards[i].equals("innkeeper.jpg") || characterCards[i].equals("clown.jpg")
                        || characterCards[i].equals("princess.jpg")){
                    switch(i) {
                        case 0:
                            msg.setFirstCardStudents(controller.getModel().getCharacterCards().get(i).getStudents().get());
                            break;
                        case 1:
                            msg.setSecondCardStudents(controller.getModel().getCharacterCards().get(i).getStudents().get());
                            break;
                        case 2:
                            msg.setThirdCardStudents(controller.getModel().getCharacterCards().get(i).getStudents().get());
                            break;
                    }
                }

            }
            sendAll(msg);
        }
        controller.fillCloud();
        BoardChange change=null;
        if(numberOfPlayers==2){
            change=new BoardChange(controller.getModel().getClouds().get(0).getStudents(),
                    controller.getModel().getClouds().get(1).getStudents());

        }else if(numberOfPlayers==3){
            change=new BoardChange(controller.getModel().getClouds().get(0).getStudents(),
                    controller.getModel().getClouds().get(1).getStudents(),
                    controller.getModel().getClouds().get(2).getStudents());
        }
        sendAll(new UpdateMessage(change));

        /*while(true){

        }*/
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getCurrentPlayerPosition() {
        return currentPlayerPosition;
    }

    public void setCurrentPlayerPosition(int currentPlayerPosition) {
        this.currentPlayerPosition = currentPlayerPosition;
    }

    public List<ClientHandler> getPlayers() {
        return players;
    }

    public ClientHandler getClientByPlayerID(int playerID){
        ClientHandler ret=null;
        for(ClientHandler c: players){
            if(c.getPlayerID()==playerID)
                ret=c;
        }
        return ret;
    }

    public void endGame(){
        server.endGame(this.gameID);
    }

    public void checkConquest(){
        if(controller.getModel().getConquest()!=null && controller.getModel().getConquest().getMergedIsland1()==null
                && controller.getModel().getConquest().getMergedIsland2()==null){
            BoardChange change=new BoardChange(controller.getModel().getConquest().getConqueror(),
                    controller.getModel().getConquest().getConqueredIsland());
            sendAll(new UpdateMessage(change));
        }else if(controller.getModel().getConquest()!=null && (controller.getModel().getConquest().getMergedIsland1()!=null
                || controller.getModel().getConquest().getMergedIsland2()!=null)){
            sendAll(new UpdateMessage((new BoardChange(controller.getModel().getConquest().getConqueror(),
                    controller.getModel().getConquest().getConqueredIsland(),controller.getModel().getConquest().getMergedIsland1(),
                    controller.getModel().getConquest().getMergedIsland2()))));
        }
        if(controller.checkEndGame()){
            controller.setWinners(controller.getModel().getWinner());
            for (Player p : controller.getWinners()) {
                sendTo(new YouWin(), getClientByPlayerID(p.getPlayerID()));
                sendAllExcept(new OtherWin(p.getNickname()), getClientByPlayerID(p.getPlayerID()));
                endGame();
            }
        }
    }

    @Override
    public void run() {
        setup();
        controller.setFirstPlayer(players.get(0).getPlayerID());
        controller.getModel().setCurrentPlayer(players.get(0).getPlayerID());
        setCurrentPlayerPosition(0);
        sendTo(new YourTurn(),players.get(0));
        sendAllExcept(new IsTurnOfPlayer(players.get(0).getNickname()),players.get(0));
        String[] cards=new String[10];
        for(int i=0; i<10;i++){
            cards[i]=controller.getModel().getPlayerByID(players.get(0).getPlayerID()).getAssistantCards().get(i).getName();
        }
        sendTo(new SelectAssistantCard(cards),players.get(0));

    }

    @Override
    public String toString() {
        return gameID + ". " + name + " " + players.size() + "/" + numberOfPlayers + " players " + (expertMode ? ANSI_RED + "Expert mode" + ANSI_RESET : ANSI_GREEN + "Base mode" + ANSI_RESET);
    }
}
