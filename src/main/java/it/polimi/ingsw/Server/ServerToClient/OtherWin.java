package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.ClientToServer.ClientToServerMessage;
import it.polimi.ingsw.Client.View;

import java.io.IOException;

public class OtherWin implements ServerToClientMessage {
    private String msg;

    public OtherWin(String nickname){
        msg=nickname+ " won!\n";
    }

    public  String getMsg() {
        return msg;
    }

    @Override
    public void handle(View view) throws IOException {
        view.youWin();
    }
}
