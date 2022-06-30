package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import java.io.IOException;

/**
 * implementation of a message from server to client to inform the client of the winning of another player
 * @author Federica Tommasini, Angelo Zagami, Giuseppe Bonanno
 */
public class OtherPlayerWins implements ServerToClientMessage{
    private String msg;

    public OtherPlayerWins(String winnerNickname) {
        msg=winnerNickname+" won!\n";
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public void handle(View view) throws IOException {
        view.otherPlayerWins(this);
    }
}
