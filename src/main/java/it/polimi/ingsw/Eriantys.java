package it.polimi.ingsw;


import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Server.Server;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main class of Eriantys Game, allows user to start the Server or the client (CLI or GUI)
 * @author Angelo Zagami
 */
public class Eriantys
{
    /***
     * Main of the game, it starts the server or the client (CLI or GUI)
     * @author Angelo Zagami
     * @param args
     */
    public static void main( String[] args ) {
        int choose = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println( "Welcome to Eriantys!\n" +
                "0. SERVER\n1. CLIENT (CLI INTERFACE)\n2. CLIENT (GUI INTERFACE)\n"
        );
        System.out.print("> ");
        try {
            choose = scanner.nextInt();
        }
        catch (InputMismatchException e){
            System.err.println("Please insert a numeric argument! Application will now close.");
            System.exit(-1);
        }

        switch (choose){
            case 0: Server.main(null);
                    break;
            case 1: Client.main(null, "CLI");
                    break;
            case 2: Client.main(null, "GUI");
                    break;
            default:System.err.println("Invalid argument");
        }
    }
}
