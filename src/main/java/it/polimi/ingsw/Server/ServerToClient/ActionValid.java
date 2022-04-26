package it.polimi.ingsw.Server.ServerToClient;

public class ActionValid implements ServerToClientMessage{
    private static String msg="the action is valid";

    public static String getMsg() {
        return msg;
    }
}
