package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.State.MoveMotherNatureState;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.ServerToClient.*;

public class MoveMotherNature implements ClientToServerMessage{
    private int steps;

    public MoveMotherNature(int steps){
        this.steps=steps;
    }

    public void handleMessage(GameHandler game, ClientHandler player){
        Action action=new Action();
        try{
            System.out.println("Old steps: "+steps);
            if(steps<=0){
                steps+=game.getController().getModel().getIslandSize();
            }
            System.out.println("New Steps: "+steps);
            action.setMotherNatureSteps(steps);
            game.getController().setState(new MoveMotherNatureState());
            game.getController().doAction(action);
            System.out.println("DEBUG MN 1");
            game.sendAll(new UpdateMessage(new BoardChange(steps)));
            System.out.println("DEBUG MN 2");
            game.checkConquest(false);
            if(game.getController().getModel().isLastRound()
                && game.getController().getModel().isEmptyClouds()){
                int pos = 0;
                for (int i = 0; i < game.getController().getTurnOrder().length; i++) {
                    if (game.getController().getTurnOrder()[i] == game.getController().getModel().getCurrentPlayer())
                        pos = i;
                }

                if (pos == game.getController().getTurnOrder().length - 1 || game.getController().checkEndGame()) {
                    game.getController().setWinners(game.getController().getModel().getWinner());
                    for (Player p : game.getController().getWinners()) {
                        game.sendTo(new YouWin(), game.getClientByPlayerID(p.getPlayerID()));
                        game.sendAllExcept(new OtherPlayerWins(p.getNickname()), game.getClientByPlayerID(p.getPlayerID()));
                        game.endGame();
                    }
                }else{
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
            if(!game.getController().checkEndGame()){
                game.sendTo(new ChooseOption(OptionType.CHOOSECLOUD,game.isExpertMode()),player);
                System.out.println("DEBUG MN 3");
            }/*else {
                System.out.println("ENDGAME");
                game.getController().setWinners(game.getController().getModel().getWinner());
                for (Player p : game.getController().getWinners()) {
                    System.out.println(p.getNickname());
                    game.sendTo(new YouWin(), game.getClientByPlayerID(p.getPlayerID()));
                    game.sendAllExcept(new OtherPlayerWins(p.getNickname()), game.getClientByPlayerID(p.getPlayerID()));
                    //endGame();
                }
            }*/

        }catch(IllegalArgumentException e){
            e.printStackTrace();
            game.sendTo(new ActionNonValid(),player);
            game.sendTo(new ChooseOption(OptionType.MOVENATURE,game.isExpertMode()),player);
        }

    }
}
