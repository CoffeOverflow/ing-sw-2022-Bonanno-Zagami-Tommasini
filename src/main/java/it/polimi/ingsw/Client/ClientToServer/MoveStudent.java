package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.State.MoveMotherNatureState;
import it.polimi.ingsw.Controller.State.MoveStudentsState;
import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.ServerToClient.*;

/**
 * MoveStudent class
 * implementation of a message from client to server to indicate the student to move and where to move it
 * @author Federica Tommasini, Angelo Zagami
 */
public class MoveStudent implements ClientToServerMessage{

    private int numOfInstances;
    private MoveTo moveTo;

    private Color studentColor;

    private int islandPosition;

    public MoveStudent(MoveTo move, Color color, int position,int n){
        moveTo=move;
        studentColor=color;
        islandPosition=position;
        numOfInstances=n;
    }

    public MoveStudent(MoveTo move, Color color, int n){
        moveTo=move;
        studentColor=color;
        numOfInstances=n;
    }

    public void handleMessage(GameHandler game, ClientHandler player){
        /*
         * set the state of the controller and call the doAction method to implement the move of the student
         */
        Action action=new Action();
        action.setMove(moveTo);
        action.setColorStudent(studentColor);
        action.setPosIsland(islandPosition);
        if(!(game.getController().getState() instanceof MoveStudentsState)){
            game.getController().setState(new MoveStudentsState());
        }
        int money = game.getController().getModel().getPlayerByID(game.getController().getModel().getCurrentPlayer()).getMoney();
        try{
            game.getController().doAction(action);
        }catch(IllegalArgumentException e){
            /*
             * if the move is not valid, ask the player to choose a new move
             */
            game.sendTo(new ActionNonValid(), player);
            game.sendTo(new ChooseOption(OptionType.MOVESTUDENTS,game.isExpertMode()),player);
        }

        /*
         * check if the player has gained money with the move and in such case send a message to all the clients
         * to inform them
         */
        if(money != game.getController().getModel().getPlayerByID(game.getController().getModel().getCurrentPlayer()).getMoney()){
            game.sendAll(new AddMoney(game.getController().getModel().getCurrentPlayer()));
        }
        /*
         * send an update message to all the clients to make them show the changes
         */
        BoardChange change=new BoardChange(moveTo,studentColor,islandPosition,game.getController().getModel().getCurrentPlayer());
        game.sendAll(new UpdateMessage(change));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /*
         * if the number of moved students is less than 2 (in case of 2 players) or less than 3 (in case of 3 players)
         * send a message to the player asking to choose another student to move, otherwise send a message asking
         * to move mother nature
         */
        if(numOfInstances<2 && game.getNumberOfPlayers()==2)
            game.sendTo(new ChooseOption(OptionType.MOVESTUDENTS,game.isExpertMode()),player);
        else if(numOfInstances<3 && game.getNumberOfPlayers()==3)
            game.sendTo(new ChooseOption(OptionType.MOVESTUDENTS,game.isExpertMode()),player);
        else if((numOfInstances==2 && game.getNumberOfPlayers()==2) || (numOfInstances==3 && game.getNumberOfPlayers()==3)) {
            game.sendTo(new ChooseOption(OptionType.MOVENATURE,game.isExpertMode()), game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
            game.getController().setState(new MoveMotherNatureState());
        }

    }
}
