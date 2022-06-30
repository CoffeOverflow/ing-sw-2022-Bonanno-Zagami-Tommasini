package it.polimi.ingsw.Client.ClientToServer;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Controller.State.DecideFirstPlayerState;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.ServerToClient.*;

/**
 * PlayAssistantCard class
 * implementation of a message from client to server to indicate the assistant card to play chosen by the player
 * @author Federica Tommasini
 */
public class PlayAssistantCard implements ClientToServerMessage{

    private final int cardValue;

    public PlayAssistantCard(int card){
        cardValue=card;
    }


    public void handleMessage(GameHandler game, ClientHandler player){
        /*
         * save in a map attribute of the controller the association between the card played and the player
         * then set the state of the controller to check if the card has not been played by other previous players in
         * the turn and to define the order of the action phase of the game when the last player selects the card
         */
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
            game.sendAllExcept(new PlayerPlayAssistantCard(player.getPlayerID(), card), player);

            if(contr.getCurrentCardPlayers().size()==game.getNumberOfPlayers()) {
                /*
                 * the player is the last of the turn: send a message to the next player to allow him to move the students
                 */
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
                /*
                 * the player is not the last of the turn: send a message to the next player to allow him to select
                 * the assistant card to play
                 */
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
            /*
             * if the card has already been played by another player, send a message asking to select another one
             */
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
