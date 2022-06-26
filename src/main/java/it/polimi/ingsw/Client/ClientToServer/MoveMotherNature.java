package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.State.MoveMotherNatureState;
import it.polimi.ingsw.Controller.State.MoveStudentsState;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.ServerToClient.*;

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
            if(steps<=0){
                steps+=game.getController().getModel().getIslandSize();
            }
            action.setMotherNatureSteps(steps);
            game.getController().setState(new MoveMotherNatureState());
            game.getController().doAction(action);
            game.sendAll(new UpdateMessage(new BoardChange(steps)));
            game.checkConquest();
            if(!game.getController().checkEndGame()){
                game.sendTo(new ChooseOption(OptionType.CHOOSECLOUD,game.isExpertMode()),player);
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
