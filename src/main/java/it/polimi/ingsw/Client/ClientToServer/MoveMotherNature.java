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
            action.setMotherNatureSteps(steps);
            game.getController().setState(new MoveMotherNatureState());
            game.getController().doAction(action);
            game.sendAll(new UpdateMessage(new BoardChange(steps)));
            if(game.getController().getConquest()!=null && game.getController().getConquest().getMergedIsland1()==null
                && game.getController().getConquest().getMergedIsland2()==null){
                BoardChange change=new BoardChange(game.getController().getConquest().getConqueror(),
                        game.getController().getConquest().getConqueredIsland());
                game.sendAll(new UpdateMessage(change));
            }else if(game.getController().getConquest()!=null && (game.getController().getConquest().getMergedIsland1()!=null
                    || game.getController().getConquest().getMergedIsland2()!=null)){
                game.sendAll(new UpdateMessage((new BoardChange(game.getController().getConquest().getConqueror(),
                        game.getController().getConquest().getConqueredIsland(),game.getController().getConquest().getMergedIsland1(),
                        game.getController().getConquest().getMergedIsland2()))));
            }
            if(game.getController().checkEndGame()){
                game.getController().setWinners(game.getController().getModel().getWinner());
                for (Player p : game.getController().getWinners()) {
                    game.sendTo(new YouWin(), game.getClientByPlayerID(p.getPlayerID()));
                    game.sendAllExcept(new OtherWin(p.getNickname()), game.getClientByPlayerID(p.getPlayerID()));
                }
            }else{
                game.sendTo(new ChooseOption(OptionType.CHOOSECLOUD,game.isExpertMode()),player);
            }

        }catch(IllegalArgumentException e){
            e.printStackTrace();
            game.sendTo(new ActionNonValid(),player);
            game.sendTo(new ChooseOption(OptionType.MOVENATURE,game.isExpertMode()),player);
        }

    }
}
