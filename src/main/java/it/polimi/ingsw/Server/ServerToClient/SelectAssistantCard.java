package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;

/**
 * implementation of a message from server to client to ask the client to select a card to play
 * @author Federica Tommasini
 */
public class SelectAssistantCard implements ServerToClientMessage{

    private static String msg="select a card to play \n";

    private final String[] availableCards;

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
