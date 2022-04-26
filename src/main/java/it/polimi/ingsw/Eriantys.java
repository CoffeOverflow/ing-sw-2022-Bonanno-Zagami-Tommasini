package it.polimi.ingsw;


import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Server.Server;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main class of Eriantys Game
 * @author Angelo Zagami
 */
public class Eriantys
{
    public static void main( String[] args )
    {
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
        if (choose == 0) {
            Server.main(null);
        } else if(choose == 1){
            Client.main(null, "CLI");
        } else if(choose == 2){
            Client.main(null, "GUI");
        } else {
            System.err.println("Invalid argument");
        }
    }
}
