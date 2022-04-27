package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.CLI.CLI;

public class Client {
    public static void main(String[] args, String mode) {
        switch (mode){
            case "CLI": CLI.main(null);
                        break;
            case "GUI": System.err.println("GUI non ancora implementata :c");
                        break;
            default:    System.exit(-1);
        }

    }
}
