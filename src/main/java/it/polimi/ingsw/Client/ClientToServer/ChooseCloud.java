package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.State.TakeStudentsState;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.ServerToClient.*;

public class ChooseCloud implements ClientToServerMessage{

    private int cloud;

    public ChooseCloud(int cloud){
        this.cloud=cloud;
    }

    public int getCloud() {
        return cloud;
    }

    public void handleMessage(GameHandler game, ClientHandler player){
        Action action=new Action();
        action.setChooseCloud(cloud);
        game.getController().setState(new TakeStudentsState());
        game.getController().doAction(action);
        if(!game.getController().getWinners().isEmpty()) {
            for (Player p : game.getController().getWinners()) {
                game.sendTo(new YouWin(), game.getClientByPlayerID(p.getPlayerID()));
                game.sendAllExcept(new OtherWin(p.getNickname()), game.getClientByPlayerID(p.getPlayerID()));
                game.endGame();
            }
        }else{
            game.getController().getModel().endTurnOfPlayer();
            int pos=0;
            for(int i=0; i<game.getController().getTurnOrder().length;i++){
                if(game.getController().getTurnOrder()[i]==game.getController().getModel().getCurrentPlayer())
                    pos=i;
            }
            if(pos==game.getController().getTurnOrder().length-1){
                game.getController().getModel().setCurrentPlayer(game.getController().getFirstPlayer());
                game.sendTo(new YourTurn(),game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
                game.sendAllExcept(new IsTurnOfPlayer(game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()).getNickname()),game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
                String[] cards=new String[10];
                for(int i=0; i<10;i++){
                    cards[i]=game.getController().getModel().getPlayerByID(game.getPlayers().get(game.getCurrentPlayerPosition()).getPlayerID()).getAssistantCards().get(i).getName();
                }
                game.sendTo(new SelectAssistantCard(cards), game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
            }
            else{
                game.getController().getModel().setCurrentPlayer(game.getController().getTurnOrder()[pos+1]);
                game.sendAllExcept(new IsTurnOfPlayer(game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()).getNickname()),game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
                game.sendTo(new YourTurn(),game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
                game.sendTo(new ChooseOption(OptionType.MOVESTUDENTS,game.isExpertMode()),game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
            }
        }
    }
}
