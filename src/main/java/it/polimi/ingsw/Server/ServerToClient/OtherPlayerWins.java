package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;

public class OtherPlayerWins implements ServerToClientMessage{
    private String msg;
    private String winnerNickname;

    public OtherPlayerWins(String winnerNickname) {
        this.winnerNickname = winnerNickname;
        msg=winnerNickname+" won!\n";
    }

    public String getWinnerNickname() {
        return winnerNickname;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public void handle(View view) throws IOException {
        view.otherPlayerWins(this);
    }
}
