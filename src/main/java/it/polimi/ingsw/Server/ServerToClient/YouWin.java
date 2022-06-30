package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import java.io.IOException;

/***
 * Message sent to the player who win the game
 * @author Giuseppe Bonanno, Federica Tommasini, Angelo Zagami
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
