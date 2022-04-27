package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.ClientToServer.ChooseNickname;
import it.polimi.ingsw.Server.ServerToClient.RequestNickname;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Server server;
    private int playerID;
    private String nickname;

    public ClientHandler(Socket clientSocket, Server serve, int playerID) throws IOException {
        this.clientSocket = clientSocket;
        this.server = server;
        this.playerID = playerID;
        this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

    }

    public int getPlayerID(){
        return playerID;
    }

    @Override
    public void run() {
        System.out.println("Client "+getPlayerID()+ " handler started!");
        boolean confirmation = false;
        try {
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try {
            outputStream.writeObject(new RequestNickname());
            outputStream.flush();
            Object answer = inputStream.readObject();
            if(answer instanceof ChooseNickname){
                System.out.println(((ChooseNickname) answer).getNickname());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
