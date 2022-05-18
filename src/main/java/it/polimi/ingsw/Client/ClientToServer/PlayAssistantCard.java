package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.State.DecideFirstPlayerState;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.ServerToClient.GenericMessage;
import it.polimi.ingsw.Server.ServerToClient.IsTurnOfPlayer;
import it.polimi.ingsw.Server.ServerToClient.SelectAssistantCard;
import it.polimi.ingsw.Server.ServerToClient.YourTurn;

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
        AssistantCard card= game.getController().getModel().getPlayerByID(playerId).getAssistantCards().get(cardValue-1);
        game.getController().addCurrentAssistantCard(playerId,card);

        if(game.getController().getCurrentCardPlayers().size()==game.getNumberOfPlayers()) {
            game.getController().setState(new DecideFirstPlayerState());
            game.getController().doAction(null);
            //TODO send update message
        }else{

            if(game.getCurrentPlayerPosition()==game.getPlayers().size()-1){
                game.setCurrentPlayerPosition(0);
            }else{
                game.setCurrentPlayerPosition(game.getCurrentPlayerPosition()+1);
            }
            game.getController().getModel().setCurrentPlayer(game.getPlayers().get(game.getCurrentPlayerPosition()).getPlayerID());
            game.sendTo(new YourTurn(),game.getPlayers().get(game.getCurrentPlayerPosition()));

            game.sendAllExcept(new IsTurnOfPlayer(game.getPlayers().get(game.getCurrentPlayerPosition()).getNickname()),game.getPlayers().get(game.getCurrentPlayerPosition()));
            String[] cards=new String[10];
            for(int i=0; i<10;i++){
                cards[i]=game.getController().getModel().getPlayerByID(game.getPlayers().get(game.getCurrentPlayerPosition()).getPlayerID()).getAssistantCards().get(i).getName();
            }
            game.sendTo(new SelectAssistantCard(cards),game.getPlayers().get(game.getCurrentPlayerPosition()));
        }

    }
}
