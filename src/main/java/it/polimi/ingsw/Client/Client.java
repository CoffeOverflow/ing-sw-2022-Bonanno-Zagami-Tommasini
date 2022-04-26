package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.CLI.CLI;

public class Client {
    public static void main(String[] args, String mode) {
        if(mode.equals("CLI")){
            CLI.main(null);
        }
        else{
            System.err.println("GUI non ancora implementata :c");
            System.exit(-1);
        }

    }
}
