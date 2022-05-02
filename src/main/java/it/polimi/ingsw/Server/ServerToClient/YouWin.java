package it.polimi.ingsw.Server.ServerToClient;

public class YouWin implements ServerToClientMessage{

    private static String msg="Congratulations, you won!";

    public static String getMsg() {
        return msg;
    }
}
