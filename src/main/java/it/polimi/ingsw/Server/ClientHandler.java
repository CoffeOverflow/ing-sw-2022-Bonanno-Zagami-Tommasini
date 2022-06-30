package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.ClientToServer.*;
import it.polimi.ingsw.Exceptions.DuplicateNicknameException;
import it.polimi.ingsw.Exceptions.InvalidNicknameException;
import it.polimi.ingsw.Exceptions.MatchFullException;
import it.polimi.ingsw.Server.ServerToClient.*;
import it.polimi.ingsw.Server.ServerToClient.Error;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import static it.polimi.ingsw.Constants.halfTimeout;
import static it.polimi.ingsw.Constants.timeout;

/***
 * Client Handler Class
 * @author Angelo Zagami
 */
public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final Server server;
    private final int playerID;
    private String nickname;
    private GameHandler game;


    /***
     * Class construcotr
     * @param clientSocket The client socket
     * @param server The server
     * @param playerID The player ID
     * @throws IOException If there is a network error
     */
    public ClientHandler(Socket clientSocket, Server server, int playerID) throws IOException {
        this.clientSocket = clientSocket;
        this.server = server;
        this.playerID = playerID;
        this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
        this.clientSocket.setSoTimeout(timeout);
        new Thread(() -> { //Server to Client
            while(true){
                try {
                    Thread.sleep(halfTimeout);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try{
                    sendHeartbeat();
                }catch (RuntimeException e){
                    System.out.println("Client "+getPlayerID()+" "+getNickname()+" disconnected!");
                    server.removePlayer(getPlayerID());
                    if(game != null){
                        if(server.getAvailableGames().contains(game))
                            server.removeAvailableGame(game.getGameID());
                        else
                            server.endGame(game.getGameID());
                    }
                    break;
                }
            }

        }).start();
    }

    /***
     * Set the game of the client
     * @param game The game
     */
    public void setGame(GameHandler game){
        this.game = game;
    }

    /***
     * Return the ID of the players
     * @return Player ID
     */
    public int getPlayerID(){
        return playerID;
    }

    /***
     * Get the player nickname
     * @return The player nickname
     */
    public String getNickname() {
        return nickname;
    }


    /***
     * Send a message to the client
     * @param message The message to send
     */
    public synchronized void send(ServerToClientMessage message){
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * Send Heartbeat to client in order to be sure that it is connected
     */
    public synchronized void sendHeartbeat(){
        send(new ServerHeartbeat());
    }


    /***
     * Receive message from the client
     * @return The message received
     */
    public Object answer(){
        try {
            return inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * This method ask client nickname and allows client to choose or create a new game
     */
    public void setup(){
        boolean confirmation = false;
        String state = "Nickname";
        Object answer;
        do{
            switch (state){
                case "Nickname":
                    send(new RequestNickname());
                    do{
                        answer = answer();
                    }while(answer instanceof ClientHeartbeat);
                    if(answer instanceof ChooseNickname){
                        String nick = ((ChooseNickname) answer).getNickname();
                        try {
                            server.registerNickname(playerID, nick);
                            send(new ActionValid("Nickname has been register successfully!"));
                            this.nickname = nick;
                            state = "Match";
                        } catch (DuplicateNicknameException e) {
                            //System.out.println("The entered nickname has already been chosen by another player!");
                            send(new Error(ErrorsType.DUPLICATENICKNAME, "The entered nickname has already been chosen by another player!"));
                        } catch (InvalidNicknameException e) {
                            //System.out.println("Nickname invalido");
                            send(new Error(ErrorsType.INVALIDNICKNAME, "The entered nickname is invalid!"));
                        }
                    }
                    break;
                case "Match":
                    ArrayList<Integer> availableIDs = new ArrayList<>();
                    if(server.isAvailableGame()){
                        ArrayList<String> availableGames = new ArrayList<>();
                        for(GameHandler game : server.getAvailableGames()){
                            availableGames.add(game.toString());
                            availableIDs.add(game.getGameID());
                        }
                        send(new ChooseMatch(availableGames));
                    }
                    else{
                        send(new ChooseMatch());
                    }
                    do{
                        answer = answer();
                    }while(answer instanceof ClientHeartbeat);
                    if(answer instanceof SelectMatch){
                        //Controllo sull'intero
                        if(((SelectMatch) answer).getMatch() == 0){
                            state = "Setup";
                        }
                        else{
                            if(availableIDs.contains(((SelectMatch) answer).getMatch())) {
                                try {
                                    server.getAvailableGameByID(((SelectMatch) answer).getMatch()).addPlayer(this);
                                    confirmation = true;
                                }
                                catch (MatchFullException e){
                                    send(new Error(ErrorsType.CHOSENOTVALID, "The selected match is full, please select another match!"));
                                }

                            }
                            else
                                send(new Error(ErrorsType.CHOSENOTVALID, "Please enter a valid game id!"));
                        }

                    }
                    break;
                case "Setup":
                    send(new RequestSetUp());
                    do{
                        answer = answer();
                    }while(answer instanceof ClientHeartbeat);
                    if (answer instanceof SelectModeAndPlayers){
                        if(((SelectModeAndPlayers) answer).getNumberOfPlayers() == 2 || ((SelectModeAndPlayers) answer).getNumberOfPlayers() == 3){
                            server.newGame(this, ((SelectModeAndPlayers) answer).getNumberOfPlayers(), ((SelectModeAndPlayers) answer).isExpertMode());
                            confirmation = true;
                        }
                        else
                            send(new Error(ErrorsType.CHOSENOTVALID, "Please enter a valid number of players and expert mode!"));
                    }

                    break;

            }

        }while(!confirmation);
    }

    /***
     * Close the client connection
     */
    public void close(){
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * Main loop of the client handler, it receives the message and execute the handle
     */
    @Override
    public void run() {
        System.out.println("Client "+getPlayerID()+ " handler started!");
        setup();
        System.out.println("Client ID: " + getPlayerID() + " Nickname: " + getNickname() +" > Game setup completed!");
        ClientToServerMessage answer;
        while (true){
            try{
                answer = (ClientToServerMessage) answer();
            }catch (RuntimeException e){
                server.endGame(game.getGameID());
                server.removeAvailableGame(game.getGameID());
                break;
            }
            if(!(answer instanceof ClientHeartbeat))
                answer.handleMessage(game, this);
        }
    }
}
