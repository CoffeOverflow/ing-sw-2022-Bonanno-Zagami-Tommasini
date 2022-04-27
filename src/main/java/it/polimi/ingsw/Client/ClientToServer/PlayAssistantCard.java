package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.AssistantCard;

import java.util.HashMap;

public class PlayAssistantCard implements ClientToServerMessage{

    private int cardValue;

    public PlayAssistantCard(int card){
        cardValue=card;
    }

    public int getCardName() {
        return cardValue;
    }

    public void handleMessage(GameController controller){
        int playerId=controller.getModel().getCurrentPlayer();
        AssistantCard card= controller.getModel().getPlayerByID(playerId).getAssistantCards().get(cardValue);
        controller.addCurrentAssistantCard(playerId,card);
        if(controller.getCurrentCardPlayers().size()==4)
            controller.doAction(null);

    }
}
