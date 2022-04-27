package it.polimi.ingsw.Server;

import it.polimi.ingsw.Constants;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
    public Server(ServerSocket server){
        this.server = server;
        this.clientExecutor = Executors.newCachedThreadPool();
        this.nextClientID = 0;

    }
    public static void main(String[] args) {
        int port = -1;
        ServerSocket server = null;
        /*Scanner scanner = new Scanner(System.in);
        System.out.println("\nWelcome to Eriantys server!\n");
        do{
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
                ClientHandler clientHandler = new ClientHandler(client, this, getNewClientID());
                clientExecutor.submit(clientHandler);
            }
            catch (IOException e){
                System.err.println("Connection with client failed.");
            }
        }
    }

    public synchronized int getNewClientID(){
        return nextClientID++;
    }
}
