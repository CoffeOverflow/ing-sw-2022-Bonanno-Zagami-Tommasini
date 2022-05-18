package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SelectAssistantCard implements ServerToClientMessage{

    private static String msg="select a card to play \n";

    private String[] availableCards;

    public SelectAssistantCard(String[] cards){
        availableCards=cards;
    }

    public static String getMsg() {
        return msg;
    }

    public String[] getAvailableCards() {
        return availableCards;
    }

    @Override
    public void handle(View view) throws IOException {
        view.selectAssistantCard(this);
    }
}
