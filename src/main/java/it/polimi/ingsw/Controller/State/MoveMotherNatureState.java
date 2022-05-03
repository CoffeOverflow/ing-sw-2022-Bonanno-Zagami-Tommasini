package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Tower;

import java.util.HashMap;
import java.util.Optional;

public class MoveMotherNatureState implements GameControllerState {
    @Override
    public void turnAction(GameController gc, Action action) {
        GameModel m=gc.getModel();
        int currentPlayer=m.getCurrentPlayer();
        if(action.getMotherNatureSteps()<=m.getCurrentCardPlayers().get(currentPlayer).getMothernatureSteps()){
            m.moveMotherNature(action.getMotherNatureSteps());
        }else{
            System.out.println("the number of steps chosen is higher than the one indicated by the assistant card");
        }
        int noEntryCards=m.getIslandByPosition(m.getMotherNaturePosition()).getNoEntryCard();
        if(noEntryCards==0)
            m.computeInfluence(m.getMotherNaturePosition());
        else m.getIslandByPosition(m.getMotherNaturePosition()).setNoEntryCard(noEntryCards-1);

    }

}
