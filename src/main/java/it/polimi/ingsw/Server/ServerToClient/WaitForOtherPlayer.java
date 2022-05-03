package it.polimi.ingsw.Server.ServerToClient;

public class WaitForOtherPlayer implements ServerToClientMessage {
    private static String msg="Wait for other players";

    public static String getMsg() {
        return msg;
    }

}
