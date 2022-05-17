package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SelectAssistantCard implements ServerToClientMessage{

    private static String msg="select a card to play";

    /**
     * name of the cards mapped to an array of two elements
     * containing in the 0 position the value of the card and
     * in position 1 the steps of mother nature
     * */
    private HashMap<String, Integer[]> availableCards;

    public SelectAssistantCard(HashMap<String, Integer[]> cards){
        availableCards=cards;
    }

    public static String getMsg() {
        return msg;
    }

    public HashMap<String, Integer[]> getAvailableCards() {
        return availableCards;
    }

    @Override
    public void handle(View view) throws IOException {
        view.selectAssistantCard(this);
    }
}
