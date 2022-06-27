package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.State.MoveMotherNatureState;
import it.polimi.ingsw.Controller.State.MoveStudentsState;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.ServerToClient.*;

import java.util.EnumMap;

public class MoveMotherNature implements ClientToServerMessage{
    private int steps;

    public MoveMotherNature(int steps){
        this.steps=steps;
    }

    public int getSteps() {
        return steps;
    }

    public void handleMessage(GameHandler game, ClientHandler player){
        Action action=new Action();
        int previousIslandSize=game.getController().getModel().getIslandSize();
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
            game.checkConquest();
            if(game.getController().getModel().isLastRound()){
                //-----------------------

                //----------------------
                return;
            }
            if(!game.getController().checkEndGame()){
                game.sendTo(new ChooseOption(OptionType.CHOOSECLOUD,game.isExpertMode()),player);
                System.out.println("DEBUG MN 3");
            }else {
                System.out.println("ENDGAME");
                game.getController().setWinners(game.getController().getModel().getWinner());
                for (Player p : game.getController().getWinners()) {
                    System.out.println(p.getNickname());
                    game.sendTo(new YouWin(), game.getClientByPlayerID(p.getPlayerID()));
                    game.sendAllExcept(new OtherPlayerWins(p.getNickname()), game.getClientByPlayerID(p.getPlayerID()));
                    //endGame();
                }
            }

        }catch(IllegalArgumentException e){
            e.printStackTrace();
            game.sendTo(new ActionNonValid(),player);
            game.sendTo(new ChooseOption(OptionType.MOVENATURE,game.isExpertMode()),player);
        }

    }
}
