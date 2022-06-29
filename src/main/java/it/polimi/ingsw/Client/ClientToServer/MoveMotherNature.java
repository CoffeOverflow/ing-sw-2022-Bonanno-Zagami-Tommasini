package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.State.MoveMotherNatureState;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.ServerToClient.*;

/**
 * MoveMotherNature class
 * implementation of a message from client to server to indicate the steps to move mother nature chosen by the player
 * @author Federica Tommasini, Giuseppe Bonanno
 */
public class MoveMotherNature implements ClientToServerMessage{
    private int steps;

    public MoveMotherNature(int steps){
        this.steps=steps;
    }

    public void handleMessage(GameHandler game, ClientHandler player){

        Action action=new Action();
        try{
            /*
             * set the state of the controller and execute the doAction method to move mother nature of the steps
             * indicated by the player in the message
             */
            if(steps<=0){
                steps+=game.getController().getModel().getIslandSize();
            }
            action.setMotherNatureSteps(steps);
            game.getController().setState(new MoveMotherNatureState());
            game.getController().doAction(action);

            /*
             * send an update message to inform the clients of the moving of mother nature
             * and check if any island has been conquered with such move
             */
            game.sendAll(new UpdateMessage(new BoardChange(steps)));
            game.checkConquest(false);

            /*
             * check if the turn is set to be the last round and if the clouds are empty
             */
            if(game.getController().getModel().isLastRound()
                && game.getController().getModel().isEmptyClouds()){

                int pos = 0;
                for (int i = 0; i < game.getController().getTurnOrder().length; i++) {
                    if (game.getController().getTurnOrder()[i] == game.getController().getModel().getCurrentPlayer())
                        pos = i;
                }

                /*
                 * if the current player is the last of the turn, or if one of the conditions for the immediate end
                 * of the game is verified, compute the winners and send final messages
                 */
                if (pos == game.getController().getTurnOrder().length - 1 || game.getController().checkEndGame()) {
                    game.getController().setWinners(game.getController().getModel().getWinner());
                    for (Player p : game.getController().getWinners()) {
                        game.sendTo(new YouWin(), game.getClientByPlayerID(p.getPlayerID()));
                        game.sendAllExcept(new OtherPlayerWins(p.getNickname()), game.getClientByPlayerID(p.getPlayerID()));
                        game.endGame();
                    }
                }else{
                    /*
                     * if not the last player of the turn, send a message to allow the next player to move the students
                     */
                    game.getController().getModel().setCurrentPlayer(game.getController().getTurnOrder()[pos + 1]);
                    game.sendAllExcept(new IsTurnOfPlayer(
                                    game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()).getNickname()),
                            game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
                    game.sendTo(new YourTurn(), game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
                    game.sendTo(new ChooseOption(OptionType.MOVESTUDENTS, game.isExpertMode()),
                            game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
                }
                return;
            }

            /*
             * if it is not the last round, or the clouds are not empty, check the condition of immediate end of the
             * game and if is it false, send a message to the current player to make him choose the cloud
             */
            if(!game.getController().checkEndGame()){
                game.sendTo(new ChooseOption(OptionType.CHOOSECLOUD,game.isExpertMode()),player);
            }

        }catch(IllegalArgumentException e){
            /*
             * if the steps are not valid, according to the card played, send a new message asking to choose
             * the steps again
             */
            game.sendTo(new ActionNonValid(),player);
            game.sendTo(new ChooseOption(OptionType.MOVENATURE,game.isExpertMode()),player);
        }

    }
}
