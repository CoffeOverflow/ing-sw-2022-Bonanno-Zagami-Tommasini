package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.State.TakeStudentsState;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.ServerToClient.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class ChooseCloud implements ClientToServerMessage{

    private int cloud;

    public ChooseCloud(int cloud){
        this.cloud=cloud;
    }

    public int getCloud() {
        return cloud;
    }

    public void handleMessage(GameHandler game, ClientHandler player){

        try {
            Action action = new Action();
            action.setChooseCloud(cloud);
            game.getController().setState(new TakeStudentsState());
            BoardChange clearChange=new BoardChange(game.getController().getModel().getClouds().get(cloud).getStudents(),cloud,
                    player.getPlayerID());
            game.getController().doAction(action);
            //GESTIRE SE è ULTIMO GIOCATORE SETTARE I VALORI SULLE CLOUD!
            game.sendAll(new UpdateMessage(clearChange));
            if (!game.getController().getWinners().isEmpty()) {
                for (Player p : game.getController().getWinners()) {
                    game.sendTo(new YouWin(), game.getClientByPlayerID(p.getPlayerID()));
                    game.sendAllExcept(new OtherWin(p.getNickname()), game.getClientByPlayerID(p.getPlayerID()));
                    game.endGame();
                }
            } else {
                game.getController().getModel().endTurnOfPlayer();
                int pos = 0;
                for (int i = 0; i < game.getController().getTurnOrder().length; i++) {
                    if (game.getController().getTurnOrder()[i] == game.getController().getModel().getCurrentPlayer())
                        pos = i;
                }

                if (pos == game.getController().getTurnOrder().length - 1) {
                    if(game.getController().getModel().isLastRound()){
                        game.getController().setWinners(game.getController().getModel().getWinner());
                    }
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
                    game.getController().getModel().setCurrentPlayer(game.getController().getTurnOrder()[pos + 1]);
                    game.sendAllExcept(new IsTurnOfPlayer(
                                    game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()).getNickname()),
                            game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
                    game.sendTo(new YourTurn(), game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
                    game.sendTo(new ChooseOption(OptionType.MOVESTUDENTS, game.isExpertMode()),
                            game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
                }
            }
        }
        catch (IllegalArgumentException e){
            game.sendTo(new ActionNonValid(),player);
            game.sendTo(new ChooseOption(OptionType.CHOOSECLOUD,game.isExpertMode()),player);
        }
    }
}
