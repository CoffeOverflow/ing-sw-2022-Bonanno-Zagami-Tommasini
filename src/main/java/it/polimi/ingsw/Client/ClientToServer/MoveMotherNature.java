package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.State.MoveMotherNatureState;
import it.polimi.ingsw.Controller.State.MoveStudentsState;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.ServerToClient.ActionNonValid;
import it.polimi.ingsw.Server.ServerToClient.ChooseOption;
import it.polimi.ingsw.Server.ServerToClient.OptionType;

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
        try{
            action.setMotherNatureSteps(steps);
            game.getController().setState(new MoveMotherNatureState());
            game.getController().doAction(action);
        }catch(IllegalArgumentException e){
            game.sendTo(new ActionNonValid(),game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
            game.sendTo(new ChooseOption(OptionType.MOVENATURE),game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
        }

    }
}
