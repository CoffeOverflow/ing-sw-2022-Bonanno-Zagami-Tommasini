package it.polimi.ingsw.Server;

import it.polimi.ingsw.Constant;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Server class
 * @author Angelo Zagami
 */
public class Server {
    private static ServerSocket server;
    public static void main(String[] args) {
        int port = -1;
        Scanner scanner = new Scanner(System.in);
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
        }while (port < 1024);
        Constant.setPort(port);
        System.out.println("Opening server on port "+Constant.getPort());
        try{
            server = new ServerSocket(Constant.getPort());
        }
        catch (IOException e){
            System.err.println("Error opening server!");
            System.exit(-1);
        }

        while(true){
            try{
                Socket client = server.accept();
                /*Thread clientConnection = new Thread(() -> {

                    });
                clientConnection.start();*/
                //QUI ACCETTAZIONE CLIENT
            }
            catch (IOException e){
                System.err.println("Connection with client failed.");
            }
        }

    }

}
