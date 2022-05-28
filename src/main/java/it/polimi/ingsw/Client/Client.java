package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;

import java.io.IOException;

public class Client {
    public static void main(String[] args, String mode) {
        switch (mode){
            case "CLI": CLI.main(null);
                        break;
            case "GUI": GUI.main(null);
                        break;
            default:    System.exit(-1);
        }

    }
}
