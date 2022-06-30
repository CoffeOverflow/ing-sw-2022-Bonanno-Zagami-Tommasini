package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import java.io.IOException;

/**
 * implementation of a message from server to client to inform the client that he won
 * @author Federica Tommasini
 */
public class YouWin implements ServerToClientMessage{

    private static String msg="Congratulations, you won!";

    public static String getMsg() {
        return msg;
    }

    @Override
    public void handle(View view) throws IOException {
        view.showMessage(this.getMsg());
        view.youWin();
    }
}
