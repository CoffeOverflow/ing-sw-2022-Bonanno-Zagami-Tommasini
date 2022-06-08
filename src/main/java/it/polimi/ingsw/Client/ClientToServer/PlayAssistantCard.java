package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Controller.State.DecideFirstPlayerState;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.ServerToClient.*;

public class PlayAssistantCard implements ClientToServerMessage{

    private int cardValue;

    public PlayAssistantCard(int card){
        cardValue=card;
    }

    public int getCardName() {
        return cardValue;
    }

    public void handleMessage(GameHandler game, ClientHandler player){
        GameController contr=game.getController();
        GameModel model=contr.getModel();
        int playerId= model.getCurrentPlayer();
        AssistantCard card=null;
        for(AssistantCard c: model.getPlayerByID(playerId).getAssistantCards()){
            if(c.getValue()==cardValue)
                card=c;
        }
        contr.addCurrentAssistantCard(playerId,card);
        contr.setState(new DecideFirstPlayerState());
        try{
            contr.doAction(null);
            if(contr.getCurrentCardPlayers().size()==game.getNumberOfPlayers()) {
                contr.getCurrentCardPlayers().clear();
                game.sendTo(new YourTurn(),game.getClientByPlayerID(contr.getTurnOrder()[0]));
                game.sendAllExcept(new IsTurnOfPlayer(model.getPlayerByID(contr.getTurnOrder()[0]).getNickname()),
                        game.getClientByPlayerID(contr.getTurnOrder()[0]));
                game.sendTo(new ChooseOption(OptionType.MOVESTUDENTS,game.isExpertMode()),game.getClientByPlayerID(contr.getTurnOrder()[0]));
                model.setCurrentPlayer(contr.getTurnOrder()[0]);
                for(int i=0; i<game.getPlayers().size();i++){
                    if(game.getPlayers().get(i).getPlayerID()==contr.getTurnOrder()[0])
                        game.setCurrentPlayerPosition(i);
                }
            }else{
                if(game.getCurrentPlayerPosition()==game.getPlayers().size()-1){
                    game.setCurrentPlayerPosition(0);
                }else{
                    game.setCurrentPlayerPosition(game.getCurrentPlayerPosition()+1);
                }
                model.setCurrentPlayer(game.getPlayers().get(game.getCurrentPlayerPosition()).getPlayerID());
                game.sendTo(new YourTurn(),game.getPlayers().get(game.getCurrentPlayerPosition()));

                game.sendAllExcept(new IsTurnOfPlayer(game.getPlayers().get(game.getCurrentPlayerPosition()).getNickname()),game.getPlayers().get(game.getCurrentPlayerPosition()));
                String[] cards=new String[model.getPlayerByID(game.getPlayers().get(game.getCurrentPlayerPosition()).getPlayerID()).getAssistantCards().size()];
                for(int i=0; i< cards.length;i++){
                    cards[i]=model.getPlayerByID(game.getPlayers().get(game.getCurrentPlayerPosition()).getPlayerID()).getAssistantCards().get(i).getName();
                }
                game.sendTo(new SelectAssistantCard(cards),game.getPlayers().get(game.getCurrentPlayerPosition()));
            }
        }catch(IllegalArgumentException e){
            String[] cards=new String[model.getPlayerByID(playerId).getAssistantCards().size()];
            for(int i=0; i<cards.length;i++){
                cards[i]=model.getPlayerByID(game.getPlayers().get(game.getCurrentPlayerPosition()).getPlayerID()).getAssistantCards().get(i).getName();
            }

            game.sendTo(new ActionNonValid(), player);
            game.sendTo(new SelectAssistantCard(cards),player);
            contr.getCurrentCardPlayers().remove(playerId);
        }

    }
}
