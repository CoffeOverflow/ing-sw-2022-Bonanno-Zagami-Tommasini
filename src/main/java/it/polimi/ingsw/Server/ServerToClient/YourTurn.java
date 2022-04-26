package it.polimi.ingsw.Server.ServerToClient;

public class YourTurn implements ServerToClientMessage{

    private static String msg="it's your turn";

    public static String getMsg() {
        return msg;
    }
}
