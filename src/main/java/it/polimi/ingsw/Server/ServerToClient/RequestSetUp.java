package it.polimi.ingsw.Server.ServerToClient;

public class RequestSetUp implements ServerToClientMessage{

    private static String msg="Select the number of players and the mode of the match";


    public static String getMsg() {
        return msg;
    }
}
