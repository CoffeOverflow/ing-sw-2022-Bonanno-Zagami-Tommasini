package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.View;
import java.io.IOException;

/**
 * implementation of a message from server to client to make the game start
 * @author Angelo Zagami
 */
public class GameIsStarting implements ServerToClientMessage{
    private final String message;

    public GameIsStarting(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void handle(View view) throws IOException {
        if(view instanceof GUI){
            GUI gui = (GUI) view;
            gui.startGame();
            return;
        }
        view.showMessage(message + "\n");
    }
}
