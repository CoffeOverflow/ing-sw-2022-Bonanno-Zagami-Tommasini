package it.polimi.ingsw.Server.ServerToClient;

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
}
