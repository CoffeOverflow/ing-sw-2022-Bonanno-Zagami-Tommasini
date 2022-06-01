package it.polimi.ingsw.Server;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Exceptions.DuplicateNicknameException;
import it.polimi.ingsw.Exceptions.InvalidNicknameException;
import it.polimi.ingsw.Exceptions.MatchFullException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server class
 * @author Angelo Zagami
 */
public class Server implements Runnable{
    private ServerSocket server;
    private ExecutorService clientExecutor;
    private int nextClientID;
    private int nextGameID;
    private HashMap<Integer, String> nicknameByID;
    private HashMap<Integer, GameHandler> availableGames;
    private HashMap<Integer, GameHandler> activeGames;
    public Server(ServerSocket server){
        this.server = server;
        this.clientExecutor = Executors.newCachedThreadPool();
        this.nextClientID = 0;
        this.nextGameID = 1;
        this.nicknameByID = new HashMap<Integer, String>();
        this.availableGames = new HashMap<Integer, GameHandler>();
        this.activeGames = new HashMap<Integer, GameHandler>();

    }
    public static void main(String[] args) {
        int port = -1;
        ServerSocket server = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nWelcome to Eriantys server!\n");
        /*do{
            System.out.print("Enter the server port > ");
            try {
                port = scanner.nextInt();
            }
            catch (InputMismatchException e){
                System.err.println("Please insert a numeric argument! Application will now close.");
                System.exit(-1);
            }
            if (port < 1024) {
                System.err.println("Error: ports accepted started from 1024! Please insert a new value.");
            }
        }while (port < 1024);*/
        Constants.setPort(2000);
        System.out.println("Opening server on port "+ Constants.getPort());
        try{
            server = new ServerSocket(Constants.getPort());
        }
        catch (IOException e){
            System.err.println("Error opening server!");
            System.exit(-1);
        }
        System.out.println("Server started on port "+Constants.getPort());
        Thread serverThread = new Thread(new Server(server));
        serverThread.start();

    }

    @Override
    public void run() {
        while(true){
            try{
                Socket client = server.accept();
                int id = getNewClientID();
                ClientHandler clientHandler = new ClientHandler(client, this, id);
                clientExecutor.submit(clientHandler);
                nicknameByID.put(id, null);
            }
            catch (IOException e){
                System.err.println("Connection with client failed.");
            }
        }
    }

    public synchronized int getNewClientID(){
        return nextClientID++;
    }
    public synchronized int getNewGameID(){
        return nextGameID++;
    }

    public synchronized void registerNickname(int id, String nickname) throws DuplicateNicknameException, InvalidNicknameException {
        if(nicknameByID.containsValue(nickname))
            throw new DuplicateNicknameException();
        if(nickname.length() < 3)
            throw new InvalidNicknameException();
        nicknameByID.put(id, nickname);
    }

    public synchronized ArrayList<GameHandler> getAvailableGames(){
        return new ArrayList<>(this.availableGames.values());
    }

    public synchronized boolean isAvailableGame(){
        return availableGames.size() > 0;
    }

    public synchronized void removeAvailableGame(int game){
        availableGames.remove(game);
    }

    public synchronized void newGame(ClientHandler firstplayer, int numberOfPlayer, boolean expertMode){
        int id = getNewGameID();
        GameHandler newGame = new GameHandler(id, nicknameByID.get(firstplayer.getPlayerID())+"'s match", numberOfPlayer, expertMode, this);
        try {
            newGame.addPlayer(firstplayer);
        }
        catch (MatchFullException e){

        }
        availableGames.put(id, newGame);
    }

    public GameHandler getAvailableGameByID(int id) throws MatchFullException {
        if(availableGames.containsKey(id))
            return availableGames.get(id);
        else
            throw new MatchFullException();
    }
    public synchronized void addActiveGame(GameHandler game){
        this.activeGames.put(game.getGameID(), game);
    }

    public synchronized void endGame(int gameID){
        GameHandler game = activeGames.get(gameID);
        for(ClientHandler player : game.getPlayers()){
            nicknameByID.remove(player.getPlayerID());
            player.close();
        }
        activeGames.remove(gameID);
    }

    public synchronized void removePlayer(int id){
        nicknameByID.remove(id);
    }
}
