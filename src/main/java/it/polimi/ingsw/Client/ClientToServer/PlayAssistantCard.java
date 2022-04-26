package it.polimi.ingsw.Client.ClientToServer;

public class PlayAssistantCard implements ClientToServerMessage{

    private String cardName;

    public PlayAssistantCard(String card){
        cardName=card;
    }

    public String getCardName() {
        return cardName;
    }
}
