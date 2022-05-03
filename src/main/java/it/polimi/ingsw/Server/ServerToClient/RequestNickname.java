package it.polimi.ingsw.Server.ServerToClient;

public class RequestNickname implements ServerToClientMessage{
    private static String msg="Choose a nickname";

    public static String getMsg() {
        return msg;
    }
}
