package it.polimi.ingsw.Server;

import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Exceptions.MatchFullException;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Wizards;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Server.ServerToClient.*;
import it.polimi.ingsw.Server.ServerToClient.Error;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static it.polimi.ingsw.Constants.*;

/***
 * Game Handler Class
 * @author Giuseppe Bonanno, Federica Tommasini, Angelo Zagami
 */
public class GameHandler implements Runnable{
    private final int gameID;
    private final String name;
    private final int numberOfPlayers;

    private final boolean             expertMode;
    private final List<ClientHandler> players;

    private int currentPlayerPosition;
    private final Server server;
    private final GameController controller;
    private final ArrayList<Wizards> wizards = new ArrayList<>(List.of(Wizards.values()));
    private int ready = 0;

    /***
     * Class constructor
     * @param gameID The ID of the game
     * @param name The name of the game
     * @param numberOfPlayers The number of player
     * @param expertMode The mode of the game, true if is expert mode, false otherwise
     * @param server The server
     */
    public GameHandler(int gameID, String name, int numberOfPlayers, boolean expertMode, Server server){
        this.gameID = gameID;
        this.name = name;
        this.numberOfPlayers = numberOfPlayers;
        this.expertMode = expertMode;
        this.players = new ArrayList<>();
        this.server = server;
        this.controller = new GameController(expertMode, numberOfPlayers);
    }

    /***
     * Get the controller
     * @return The controller of thr game
     */
    public GameController getController() {
        return controller;
    }

    /***
     * Add player to the game
     * @param player The player to add
     * @throws MatchFullException If the game is full
     */
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

    /***
     * Send a message to all the player in the game
     * @param message The message to send
     */
    public synchronized void sendAll(ServerToClientMessage message){
        for(ClientHandler client: players){
            client.send(message);
        }
    }

    /***
     * Send a message to all player in the game except the one that is indicated
     * @param message The message to send
     * @param player The player to exclude
     */
    public synchronized void sendAllExcept(ServerToClientMessage message, ClientHandler player){
        for(ClientHandler client: players){
            if(!client.equals(player))
                client.send(message);
        }
    }

    /***
     * Send a message to a player
     * @param message The message to send
     * @param player The player
     */
    public synchronized void sendTo(ServerToClientMessage message, ClientHandler player){
        for(ClientHandler client: players){
            if(client.equals(player))
                client.send(message);
        }
    }

    /***
     * Set the wizard chosen by the player
     * @param wizard The wizard chosen
     * @param player The player
     */
    public synchronized void playerChooseWizard(Wizards wizard, ClientHandler player){
        if(wizards.contains(wizard)){
            wizards.remove(wizard);
            controller.getModel().getPlayerByID(player.getPlayerID()).setWizard(wizard);
            ready++;
            player.send(new ActionValid());
        }
        else{
            player.send(new Error(ErrorsType.CHOSENOTVALID, "The selected wizard was choosen by another player!"));
            player.send(new SelectWizard(wizards));
        }
    }

    /***
     * Setup the game. The method is called when the game starts in order to setup the students, assistant and
     * character cards, clouds, islands.
     */
    public void setup(){
        sendAll(new GameIsStarting("The game is starting..."));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        /*
         * send message to make the players choose the wizard
         */
        sendAll(new SelectWizard(wizards));
        while(ready < numberOfPlayers){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        sendAll(new GenericMessage("Match is starting..."));
        /*
         * send message containing initial configuration of the board (position of mother nature and students
         * on islands)
         */
        HashMap<Integer, Color> mapStudentIsland=new HashMap<>();
        for(int i=0; i<12; i++){
            boolean check=false;
            for(Color c: Color.values()) {
                if (controller.getModel().getIslandByPosition(i).getStudents().get(c) != 0){
                    mapStudentIsland.put(i, c);
                    check = true;
                }
            }
            if(!check)
                mapStudentIsland.put(i,null);
        }
        sendAll(new MatchCreated(controller.getModel().getMotherNaturePosition(),mapStudentIsland) );
        /*
         * send messages to communicate other players' information and the initial configuration of the schools
         * (entrance students and towers)
         */
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
        /*
         * if the match is set to expert mode, send message to initialize the character cards, chosen randomly
         * at the beginning of the match
         */
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
        /*
         * initialize the students on the clouds
         */
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
    }

    /***
     * Get the number of players that are playing the game
     * @return The number of players
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /***
     * get the position in the list of players of the current player (needed in the planning phase to make the players
     * play in order after the first one)
     * @return the position of the current player
     */
    public int getCurrentPlayerPosition() {
        return currentPlayerPosition;
    }

    /***
     * set the position in the list of players of the current player
     * @param currentPlayerPosition position of the player
     */
    public void setCurrentPlayerPosition(int currentPlayerPosition) {
        this.currentPlayerPosition = currentPlayerPosition;
    }

    /***
     * Return the ClientHandlers of the player in the game
     * @return The list of ClientHandler of the players
     */
    public List<ClientHandler> getPlayers() {
        return players;
    }

    /***
     * Get the ClientHandler of the player that have the ID passed
     * @param playerID The ID of player
     * @return The ClientHandler of the player
     */
    public ClientHandler getClientByPlayerID(int playerID){
        ClientHandler ret=null;
        for(ClientHandler c: players){
            if(c.getPlayerID()==playerID)
                ret=c;
        }
        return ret;
    }

    /***
     * @see Server
     */
    public void endGame(){
        server.endGame(this.gameID);
    }

    /***
     * check if an island has been conquered and if any island has been merged, in such case send a message to the
     * clients to inform about the changes in the board
     * @param characterIsUsed boolean value that specify if the method is called as a consequence of using a character
     *                        card (true) or of moving mother nature (false)
     */
    public void checkConquest(boolean characterIsUsed){
        if(controller.getModel().getConquest()!=null && controller.getModel().getConquest().getMergedIsland1()==null
                && controller.getModel().getConquest().getMergedIsland2()==null){
            /*
             * island conquered, no island to merge
             */
            BoardChange change=new BoardChange(controller.getModel().getConquest().getConqueror(),
                    controller.getModel().getConquest().getConqueredIsland());
            sendAll(new UpdateMessage(change));
        }else if(controller.getModel().getConquest()!=null && (controller.getModel().getConquest().getMergedIsland1()!=null
                || controller.getModel().getConquest().getMergedIsland2()!=null)){
            /*
             * island conquered, one or two islands to merge
             */
            sendAll(new UpdateMessage((new BoardChange(controller.getModel().getConquest().getConqueror(),
                    controller.getModel().getConquest().getConqueredIsland(),controller.getModel().getConquest().getMergedIsland1(),
                    controller.getModel().getConquest().getMergedIsland2()))));
            /*
             * minPosition: island to which mother nature has to be placed at the end of the merge if the variable
             * characterIsUsed is set to false
             */
            int minPosition;
            if(controller.getModel().getConquest().getMergedIsland1()<controller.getModel().getConquest().getConqueredIsland())
                minPosition=controller.getModel().getConquest().getMergedIsland1();
            else if(null!=controller.getModel().getConquest().getMergedIsland2() &&
                    controller.getModel().getConquest().getMergedIsland2()<controller.getModel().getConquest().getConqueredIsland())
                minPosition=controller.getModel().getConquest().getMergedIsland2();
            else minPosition=controller.getModel().getConquest().getConqueredIsland();
            if(!characterIsUsed && controller.getModel().getMotherNaturePosition()!=minPosition)
              sendAll(new UpdateMessage((new BoardChange(-1))));
        }
        /*
         * reset conquest variable in model and check if any condition of immediate ending of the game is verified,
         * in such case send final messages and end the match
         */
        controller.getModel().setConquest(null);
        if(controller.checkEndGame()){
            controller.setWinners(controller.getModel().getWinner());
            for (Player p : controller.getWinners()) {
                sendTo(new YouWin(), getClientByPlayerID(p.getPlayerID()));
                sendAllExcept(new OtherPlayerWins(p.getNickname()), getClientByPlayerID(p.getPlayerID()));
                endGame();
            }
        }
    }


    /***
     * method that calls the setup method and then let the first player begin the game sending him a message to select
     * an assistant card
     */
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

    /***
     * Get the game information, the name, number of players and mode
     * @return The information of the fame
     */
    @Override
    public String toString() {
        return gameID + ". " + name + " " + players.size() + "/" + numberOfPlayers + " players " + (expertMode ? ANSI_RED + "Expert mode" + ANSI_RESET : ANSI_GREEN + "Base mode" + ANSI_RESET);
    }

    /***
     * Get the mode of the game
     * @return True if the game is expert mode, false otherwise
     */
    public boolean isExpertMode() {

        return expertMode;
    }
}
