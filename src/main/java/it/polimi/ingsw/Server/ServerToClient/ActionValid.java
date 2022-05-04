package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;

public class ActionValid implements ServerToClientMessage{
    private String msg;

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
