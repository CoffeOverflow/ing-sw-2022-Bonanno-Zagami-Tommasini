package it.polimi.ingsw.Server.ServerToClient;

import java.util.ArrayList;
import java.util.List;

public class ChooseMatch implements ServerToClientMessage{
    private static String msg="Select a match to join or type 0 for create new match > ";
    private List<String> availableMatchs;

    public ChooseMatch(List<String> matches){
        availableMatchs=matches;
    }
    public ChooseMatch(){
        availableMatchs = new ArrayList<>();
        availableMatchs.add("No match available!");
    }

    public static String getMsg() {
        return msg;
    }

    public List<String> getAvailableMatchs() {
        return availableMatchs;
    }
}
