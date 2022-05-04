package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Constants.*;

public class ChooseMatch implements ServerToClientMessage{
    private static String msg="Select a match to join or type 0 for create new match > ";
    private List<String> availableMatches;

    public ChooseMatch(List<String> matches){
        availableMatches=matches;
    }
    public ChooseMatch(){
        availableMatches = new ArrayList<>();
        availableMatches.add(ANSI_YELLOW + "No match available!" + ANSI_RESET);
    }

    public static String getMsg() {
        return msg;
    }

    public List<String> getAvailableMatchs() {
        return availableMatches;
    }

    @Override
    public void handle(View view) throws IOException {
        String games = "";
        for(String match : availableMatches)
            games += match + "\n";
        view.chooseMatch(games);
    }
}
