package it.polimi.ingsw.Server.ServerToClient;

public class ActionNonValid implements ServerToClientMessage{
    private static String msg="the action is not valid";

    public static String getMsg() {
        return msg;
    }
}
