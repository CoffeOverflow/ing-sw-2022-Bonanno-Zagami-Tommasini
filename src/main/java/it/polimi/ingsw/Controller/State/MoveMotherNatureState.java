package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.Conquest;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Tower;

import java.util.HashMap;
import java.util.Optional;

public class MoveMotherNatureState implements GameControllerState {
    @Override
    public void turnAction(GameController gc, Action action)throws IllegalArgumentException {
        GameModel m=gc.getModel();
        int currentPlayer=m.getCurrentPlayer();
        if(m.isTwoAdditionalSteps() &&
                action.getMotherNatureSteps()<=(m.getCurrentCardPlayers().get(currentPlayer).getMothernatureSteps()+2)){
            m.moveMotherNature(action.getMotherNatureSteps());
            System.out.println("DEBUG MN 4");
        }
        else if(!m.isTwoAdditionalSteps() && action.getMotherNatureSteps()<=m.getCurrentCardPlayers().get(currentPlayer).getMothernatureSteps()){
            System.out.println("DEBUG MN 5");
            m.moveMotherNature(action.getMotherNatureSteps());
            System.out.println("DEBUG MN 6");
        }else{
            throw new IllegalArgumentException("the number of steps chosen is higher than the one indicated by the assistant card");
        }
        int noEntryCards=m.getIslandByPosition(m.getMotherNaturePosition()).getNoEntryCard();
        if(noEntryCards==0){
            System.out.println("DEBUG MN 7");
            System.out.println(m.getMotherNaturePosition());
            Conquest c=m.computeInfluence(m.getMotherNaturePosition());
            System.out.println("DEBUG MN 8");
            m.setConquest(c);
        }
        else{
            m.setConquest(null);
            m.getIslandByPosition(m.getMotherNaturePosition()).setNoEntryCard(noEntryCards-1);
            int n=m.getCharactersPositions().get("herbalist.jpg");
            m.getCharacterCards().get(n).setNoEntryTiles(Optional.of(m.getCharacterCards().get(n).getNoEntryTiles().get()+1));
        }
        for(Player player: m.getPlayers()){
            System.out.println(player.getNickname() +  " Tower: " + player.getNumberOfTower());
        }

    }

}
