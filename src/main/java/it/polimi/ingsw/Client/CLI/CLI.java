package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.ClientToServer.ChooseNickname;
import it.polimi.ingsw.Client.ClientToServer.SelectMatch;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Server.Server;
import it.polimi.ingsw.Server.ServerToClient.*;
import it.polimi.ingsw.Server.ServerToClient.Error;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

import static it.polimi.ingsw.Constants.*;

public class CLI implements View, Runnable {

    private ServerHandler serverHandler;

    public CLI(ServerHandler serverHandler){
        this.serverHandler = serverHandler;
    }
    public static void main(String[] args) {
        System.out.println("\n"+Constants.ERIANTYS);
       /* Scanner scanner = new Scanner(System.in);
        System.out.print("\nInsert the server IP address > ");
        String ip = scanner.nextLine();
        System.out.print("Insert the server port > ");
        int port = scanner.nextInt();
        Constants.setIP(ip);
        Constants.setPort(port);*/
        ServerHandler server = new ServerHandler();
        Thread cliThread = new Thread(new CLI(server));
        cliThread.start();
        /*Scanner scanner = null;
        try {
            boolean confirmation = false;
            do{
                ServerToClientMessage fromServer = server.read();
                if(fromServer instanceof  RequestNickname){
                    RequestNickname msg = (RequestNickname) fromServer;
                    System.out.print(msg.getMsg()+" > ");
                    scanner = new Scanner(System.in);
                    String nickname = scanner.nextLine();
                    server.write(new ChooseNickname(nickname));
                    fromServer.handle(this, server);
                }
                if(fromServer instanceof Error){
                    Error msg = (Error) fromServer;
                    System.err.println(msg.getMessage());
                }
                if(fromServer instanceof ActionValid){
                    ActionValid msg = (ActionValid) fromServer;
                    System.out.println(msg.getMsg());
                }
                if(fromServer instanceof ChooseMatch){
                    System.out.println("\nAvailable match:");
                    ChooseMatch msg = (ChooseMatch) fromServer;
                    for(String match : msg.getAvailableMatchs())
                        System.out.println(match);
                    System.out.print("\n"+msg.getMsg());
                    int game = scanner.nextInt();
                    server.write(new SelectMatch(game));
                }
                if (fromServer instanceof GenericMessage){
                    System.out.println(((GenericMessage) fromServer).getMessage());
                }
                if(fromServer instanceof WaitForOtherPlayer){
                    WaitForOtherPlayer msg = (WaitForOtherPlayer) fromServer;
                    System.out.println(msg.getMsg());
                    confirmation = true;
                }
            }while(!confirmation);
        } catch (IOException e) {
            System.out.println("Server unreachable :c");
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        while(true){
            String fromInput = scanner.nextLine();
            if(fromInput.equals("Quit"))
                break;
        }*/

    }

    @Override
    public void requestNickname() throws IOException {
        System.out.print("Choose a nickname > ");
        Scanner scanner = new Scanner(System.in);
        String nickname = scanner.nextLine();
        serverHandler.write(new ChooseNickname(nickname));
    }

    @Override
    public void showError(String error) {
        System.out.println(ANSI_RED + error +  ANSI_RESET);
    }

    @Override
    public void actionValid(String message) {
        System.out.println(ANSI_GREEN + message +  ANSI_RESET);
    }

    @Override
    public void chooseMatch(String games) throws IOException {
        System.out.println("\nAvailable match:");
        System.out.println(games);
        System.out.print("Select a match to join or type 0 for create new match > ");
        Scanner scanner = new Scanner(System.in);
        int game = scanner.nextInt();
        serverHandler.write(new SelectMatch(game));
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void run() {
        while(true){
            ServerToClientMessage fromServer = null;
            try {
                fromServer = serverHandler.read();
                fromServer.handle(this);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
