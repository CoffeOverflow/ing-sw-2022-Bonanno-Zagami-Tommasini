package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;

public class IsTurnOfPlayer implements ServerToClientMessage{

    private String msg;

    public IsTurnOfPlayer(String playerNickname){
        msg="it's the turn of "+playerNickname+'\n';
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public void handle(View view) throws IOException {
        view.isTurnOfPlayer(msg);
    }
}
