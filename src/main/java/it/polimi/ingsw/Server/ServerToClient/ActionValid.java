package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import java.io.IOException;

/**
 * implementation of a message from server to client to inform the client of a valid action
 * @author Federica Tommasini, Angelo Zagami
 */
public class ActionValid implements ServerToClientMessage{
    private final String msg;

    public String getMsg() {
        return msg;
    }

    public ActionValid(String msg){
        this.msg = msg;
    }

    public ActionValid(){
        msg="The action is valid";
    }

    @Override
    public void handle(View view) throws IOException {
        view.actionValid(msg);
    }
}
