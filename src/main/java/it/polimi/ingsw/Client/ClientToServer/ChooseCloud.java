package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.State.TakeStudentsState;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.ServerToClient.*;
import java.util.EnumMap;

/**
 * ChooseCloud class
 * implementation of a message from client to server to indicate the cloud chosen by the player
 * @author Federica Tommasini, Angelo Zagami, Giuseppe Bonanno
 */
public class ChooseCloud implements ClientToServerMessage{

    private final int cloud;

    public ChooseCloud(int cloud){
        this.cloud=cloud;
    }

    public int getCloud() {
        return cloud;
    }

    public void handleMessage(GameHandler game, ClientHandler player){

        try {
            /*
             * prepare an action object to provide to the state of the controller, setting the integer that represents
             * the cloud, set the state of the controller that implements the action of taking the students from
             * the cloud and execute such action calling the doAction method of the controller
             */
            Action action = new Action();
            action.setChooseCloud(cloud);
            game.getController().setState(new TakeStudentsState());
            BoardChange clearChange=new BoardChange(game.getController().getModel().getClouds().get(cloud).getStudents(),cloud,
                    player.getPlayerID());
            game.getController().doAction(action);
            game.sendAll(new UpdateMessage(clearChange));
            game.getController().getModel().endTurnOfPlayer();

            /*
             * check if the player who sent the message is the last of the turn
             */
            int pos = 0;
            for (int i = 0; i < game.getController().getTurnOrder().length; i++) {
                if (game.getController().getTurnOrder()[i] == game.getController().getModel().getCurrentPlayer())
                    pos = i;
            }
            if (pos == game.getController().getTurnOrder().length - 1) {
                /*
                 * the player is the last one:
                 * check if the turn was set to be the last round and in such case compute the winners of the game
                 */
                if(game.getController().getModel().isLastRound()){
                    game.getController().setWinners(game.getController().getModel().getWinner());
                    for (Player p : game.getController().getWinners()) {
                        game.sendTo(new YouWin(), game.getClientByPlayerID(p.getPlayerID()));
                        game.sendAllExcept(new OtherPlayerWins(p.getNickname()), game.getClientByPlayerID(p.getPlayerID()));
                        game.endGame();
                        game.endGame();
                        return;
                    }
                }
                /*
                 * the turn was not the last one, so fill the clouds with new students from the bag and send an
                 * update message to the clients containing such students
                 */
                game.getController().fillCloud();
                BoardChange fillChange=null;
                EnumMap<Color, Integer> studentsCloud1=game.getController().getModel().getClouds().get(0).getStudents();
                EnumMap<Color, Integer> studentsCloud2=game.getController().getModel().getClouds().get(1).getStudents();
                if(game.getController().getModel().getClouds().size()==3){
                    EnumMap<Color, Integer> studentsCloud3=game.getController().getModel().getClouds().get(2).getStudents();
                    fillChange=new BoardChange(studentsCloud1,studentsCloud2,studentsCloud3);
                }else{
                    fillChange=new BoardChange(studentsCloud1,studentsCloud2);
                }
                game.sendAll(new UpdateMessage(fillChange));

                /*
                 * send a new message to make the next player select the assistant card he wants to play
                 */
                game.getController().getModel().setCurrentPlayer(game.getController().getFirstPlayer());
                game.sendTo(new YourTurn(), game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
                game.sendAllExcept(new IsTurnOfPlayer(
                                game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()).getNickname()),
                        game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
                String[] cards = new String[game.getController().getModel()
                        .getPlayerByID(game.getPlayers().get(game.getCurrentPlayerPosition()).getPlayerID()).getAssistantCards().size()];
                for (int i = 0; i < cards.length; i++) {
                    cards[i] = game.getController().getModel()
                            .getPlayerByID(game.getPlayers().get(game.getCurrentPlayerPosition()).getPlayerID()).getAssistantCards().get(i).getName();
                }
                game.sendTo(new SelectAssistantCard(cards),
                        game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
            } else {
                /*
                 * the player was not the last of the turn, so send a message to allow the next player to move the
                 * students
                 */
                game.getController().getModel().setCurrentPlayer(game.getController().getTurnOrder()[pos + 1]);
                game.sendAllExcept(new IsTurnOfPlayer(
                                game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()).getNickname()),
                        game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
                game.sendTo(new YourTurn(), game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
                game.sendTo(new ChooseOption(OptionType.MOVESTUDENTS, game.isExpertMode()),
                        game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
            }

        }
        catch (IllegalArgumentException e){
            /*
             * if the value chosen by the player for the cloud is invalid, send a new message asking to choose again
             */
            game.sendTo(new ActionNonValid(),player);
            game.sendTo(new ChooseOption(OptionType.CHOOSECLOUD,game.isExpertMode()),player);
        }
    }
}
