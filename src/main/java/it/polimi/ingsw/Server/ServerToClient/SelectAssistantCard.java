package it.polimi.ingsw.Server.ServerToClient;

import java.util.List;

public class SelectAssistantCard implements ServerToClientMessage{

    private static String msg="select a card to play";

    private List<String> availableCards;

    public SelectAssistantCard(List<String> cards){
        availableCards=cards;
    }

    public static String getMsg() {
        return msg;
    }

    public List<String> getAvailableCards() {
        return availableCards;
    }
}
