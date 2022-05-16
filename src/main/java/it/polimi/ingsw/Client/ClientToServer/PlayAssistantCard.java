package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;

public class PlayAssistantCard implements ClientToServerMessage{

    private int cardValue;

    public PlayAssistantCard(int card){
        cardValue=card;
    }

    public int getCardName() {
        return cardValue;
    }

    public void handleMessage(GameHandler game, ClientHandler player){
        int playerId= game.getController().getModel().getCurrentPlayer();
        AssistantCard card= game.getController().getModel().getPlayerByID(playerId).getAssistantCards().get(cardValue);
        game.getController().addCurrentAssistantCard(playerId,card);
        if(game.getController().getCurrentCardPlayers().size()==4)
            game.getController().doAction(null);

    }
}
