package it.polimi.ingsw.Server.ServerToClient;

import java.util.List;

public class SelectMatch implements ServerToClientMessage{
    private static String msg="Select a match to join";
    private List<String> availableMatchs;

    public SelectMatch(List<String> matches){
        availableMatchs=matches;
    }

    public static String getMsg() {
        return msg;
    }

    public List<String> getAvailableMatchs() {
        return availableMatchs;
    }
}
