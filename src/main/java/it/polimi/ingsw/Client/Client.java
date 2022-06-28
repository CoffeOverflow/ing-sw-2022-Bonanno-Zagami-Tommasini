package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import static it.polimi.ingsw.Constants.ANSI_GREEN;
import static it.polimi.ingsw.Constants.ANSI_RESET;

/***
 * Client Main Class
 * @author Angelo Zagami
 */
public class Client {
    /***
     * Allows user to start both che CLI and the GUI version of the game
     * @param args Main args
     * @param mode Client mode: CLI or GUI
     */
    public static void main(String[] args, String mode) {
        switch (mode){
            case "CLI": CLI.main(null);
                        break;
            case "GUI": System.out.println(ANSI_GREEN+"The GUI is starting. Good luck and have fun ;)"+ANSI_RESET);
                        GUI.main(null);
                        break;
            default:    System.exit(-1);
        }

    }
}
