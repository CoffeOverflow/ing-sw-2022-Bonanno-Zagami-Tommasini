package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.ClientToServer.ChooseNickname;
import it.polimi.ingsw.Client.ClientToServer.SelectMatch;
import it.polimi.ingsw.Exceptions.DuplicateNicknameException;
import it.polimi.ingsw.Exceptions.InvalidNicknameException;
import it.polimi.ingsw.Server.ServerToClient.*;
import it.polimi.ingsw.Server.ServerToClient.Error;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Server server;
    private int playerID;
    private String nickname;

    public ClientHandler(Socket clientSocket, Server server, int playerID) throws IOException {
        this.clientSocket = clientSocket;
        this.server = server;
        this.playerID = playerID;
        this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.inputStream = new ObjectInputStream(clientSocket.getInputStream());

    }

    public int getPlayerID(){
        return playerID;
    }

    public void send(ServerToClientMessage message){
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object answer(){
        try {
            Object answer = inputStream.readObject();
            return answer;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setup(){
        boolean confirmation = false;
        String state = "Nickname";
        Object answer = null;
        do{
            switch (state){
                case "Nickname":
                    send(new RequestNickname());
                    answer = answer();
                    if(answer instanceof ChooseNickname){
                        String nick = ((ChooseNickname) answer).getNickname();
                        try {
                            server.registerNickname(playerID, nick);
                            send(new ActionValid("Nickname has been register successfully!"));
                            state = "Match";
                        } catch (DuplicateNicknameException e) {
                            System.out.println("Nickname duplicato");
                            send(new Error(ErrorsType.DUPLICATENICKNAME, "Nickname duplicato."));
                        } catch (InvalidNicknameException e) {
                            System.out.println("Nickname invalido");
                            send(new Error(ErrorsType.INVALIDNICKNAME, "Nickname invalido."));
                        }
                    }
                    break;
                case "Match":
                    ArrayList<Integer> availableIDs = new ArrayList<Integer>();
                    if(server.isAvailableGame()){
                        ArrayList<String> availableGames = new ArrayList<String>();
                        for(GameHandler game : server.getAvailableGames()){
                            availableGames.add(game.toString());
                            availableIDs.add(game.getGameID());
                        }
                        send(new ChooseMatch(availableGames));
                    }
                    else{
                        send(new ChooseMatch());
                    }
                    answer = answer();
                    if(answer instanceof SelectMatch){
                        //Controllo sull'intero
                        if(((SelectMatch) answer).getMatch() == 0){
                            server.newGame(this);
                            confirmation = true;
                        }
                        else{
                            if(availableIDs.contains(((SelectMatch) answer).getMatch())) {
                                server.getGameByID(((SelectMatch) answer).getMatch()).addPlayer(this);
                                confirmation = true;
                            }
                            else
                                send(new Error(ErrorsType.CHOSENOTVALID, "Please enter a valid game id!"));
                        }

                    }
                    break;

            }

        }while(!confirmation);
    }

    @Override
    public void run() {
        System.out.println("Client "+getPlayerID()+ " handler started!");
        setup();
        System.out.println("Client "+getPlayerID()+" select game setup complete!");
        while (true){

        }



    }
}
