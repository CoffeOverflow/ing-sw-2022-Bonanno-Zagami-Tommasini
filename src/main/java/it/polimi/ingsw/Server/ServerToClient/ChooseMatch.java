package it.polimi.ingsw.Server.ServerToClient;

import java.util.ArrayList;
import java.util.List;

public class ChooseMatch implements ServerToClientMessage{
    private static String msg="Select a match to join or type 0 for create new match > ";
    private List<String> availableMatches;

    public ChooseMatch(List<String> matches){
        availableMatches=matches;
    }
    public ChooseMatch(){
        availableMatches = new ArrayList<>();
        availableMatches.add("No match available!");
    }

    public static String getMsg() {
        return msg;
    }

    public List<String> getAvailableMatchs() {
        return availableMatches;
    }
}
