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

import static it.polimi.ingsw.Constants.ANSI_RED;
import static it.polimi.ingsw.Constants.ANSI_RESET;

/**
 * Server class
 * @author Angelo Zagami
 */
public class Server implements Runnable{
    private final ServerSocket server;
    private final ExecutorService clientExecutor;
    private int nextClientID;
    private int nextGameID;
    private final HashMap<Integer, String> nicknameByID;
    private final HashMap<Integer, GameHandler> availableGames;
    private final HashMap<Integer, GameHandler> activeGames;

    /***
     * Server Class constructor
     * @param server The socket of Server
     */
    public Server(ServerSocket server){
        this.server = server;
        this.clientExecutor = Executors.newCachedThreadPool();
        this.nextClientID = 0;
        this.nextGameID = 1;
        this.nicknameByID = new HashMap<>();
        this.availableGames = new HashMap<>();
        this.activeGames = new HashMap<>();

    }

    /***
     * Main of the Server Class, it asks for the port and runs the server.
     * @param args Main args
     */
    public static void main(String[] args) {
        int port = -1;
        ServerSocket server = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nWelcome to Eriantys server!\n");
        do{
            System.out.print("Enter the server port > ");
            try {
                port = scanner.nextInt();
            }
            catch (InputMismatchException e){
                System.err.println(ANSI_RED+"Please insert a numeric argument! Application will now close."+ANSI_RESET);
                System.exit(-1);
            }
            if (port < 1024) {
                System.err.println(ANSI_RED+"Error: ports accepted started from 1024! Please insert a new value."+ANSI_RESET);
            }
        }while (port < 1024);
        Constants.setPort(port);
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

    /***
     * Main loop of the Server, it accepts new clients and starts their executions
     */
    @Override
    @SuppressWarnings("InfiniteLoopStatement")
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

    /**
     * When called returns the next client ID that can be used
     * @return The next client ID that can be used
     */
    public synchronized int getNewClientID(){
        return nextClientID++;
    }

    /***
     * When called returns the next game ID that can be used
     * @return The next game ID that can be used
     */
    public synchronized int getNewGameID(){
        return nextGameID++;
    }

    /***
     * Register the nickname of player on the server
     * @param id The ID of the player
     * @param nickname The nickname of the player
     * @throws DuplicateNicknameException If the chosen nickname is used by another player
     * @throws InvalidNicknameException If the chosen nickname contains spaces or the length is less than 3 characters
     */
    public synchronized void registerNickname(int id, String nickname) throws DuplicateNicknameException, InvalidNicknameException {
        if(nicknameByID.containsValue(nickname))
            throw new DuplicateNicknameException();
        if(nickname.length() < 3 || nickname.contains(" "))
            throw new InvalidNicknameException();
        nicknameByID.put(id, nickname);
    }

    /**
     * Return the available games on the server
     * @return Available games
     */
    public synchronized ArrayList<GameHandler> getAvailableGames(){
        return new ArrayList<>(this.availableGames.values());
    }

    /***
     * Get if there are available games on the server
     * @return True if there is at least one game with a player slot free, false otherwise
     */
    public synchronized boolean isAvailableGame(){
        return availableGames.size() > 0;
    }

    /***
     * Remove a game from the list of available games
     * @param game The game to remove
     */
    public synchronized void removeAvailableGame(int game){
        availableGames.remove(game);
    }

    /***
     * Create new game
     * @param firstplayer The player who created the game
     * @param numberOfPlayer The number of player of the game
     * @param expertMode The mode of the game, true if is expert mode, false otherwise
     */
    public synchronized void newGame(ClientHandler firstplayer, int numberOfPlayer, boolean expertMode){
        int id = getNewGameID();
        GameHandler newGame = new GameHandler(id, nicknameByID.get(firstplayer.getPlayerID())+"'s match", numberOfPlayer, expertMode, this);
        try {
            newGame.addPlayer(firstplayer);
        }
        catch (MatchFullException ignored){
            //Since the game is just created it can't be full
        }
        availableGames.put(id, newGame);
    }

    /***
     * Get the available game by id
     * @param id The ID of the game
     * @return The GameHandler with the ID passed
     * @throws MatchFullException If the match is full
     */
    public GameHandler getAvailableGameByID(int id) throws MatchFullException {
        if(availableGames.containsKey(id))
            return availableGames.get(id);
        else
            throw new MatchFullException();
    }

    /***
     * Add game to the list of active game
     * @param game The game to add
     */
    public synchronized void addActiveGame(GameHandler game){
        this.activeGames.put(game.getGameID(), game);
    }

    /***
     * Disconnect all the player and remove the game
     * @param gameID The ID of the game to remove
     */
    public synchronized void endGame(int gameID){
        GameHandler game = null;
        if(availableGames.containsKey(gameID))
            game = availableGames.get(gameID);
        else
            game = activeGames.get(gameID);
        if(game !=null){
            for(ClientHandler player : game.getPlayers()){
                nicknameByID.remove(player.getPlayerID());
                player.close();
            }
        }
        activeGames.remove(gameID);
        availableGames.remove(gameID);
    }

    /***
     * Remove player from the server
     * @param id The ID of player
     */
    public synchronized void removePlayer(int id){
        nicknameByID.remove(id);
    }
}
