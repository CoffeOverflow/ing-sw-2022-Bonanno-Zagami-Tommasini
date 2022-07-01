package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.Conquest;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

/**
 * @author Federica Tommasini
 * implementation of the state interface of the game controller
 */
public class MoveMotherNatureState implements GameControllerState {

    /**
     * handle the moving of mother nature
     * @param gc instance of the controller
     * @param action object containing parameters for the action
     * @throws IllegalArgumentException if the number of steps chosen by the player are not valid with respect to
     *         the card played
     */
    @Override
    public void turnAction(GameController gc, Action action)throws IllegalArgumentException {
        GameModel m=gc.getModel();
        int currentPlayer=m.getCurrentPlayer();
        /*
         * check if the number of steps are coherent with the card played and if the player has played the character
         * card which allows him to move mother nature of two additional steps and then move mother nature
         */
        if(m.isTwoAdditionalSteps() &&
                action.getMotherNatureSteps()<=(m.getCurrentCardPlayers().get(currentPlayer).getMothernaturesteps()+2)){
            m.moveMotherNature(action.getMotherNatureSteps());
        }
        else if(!m.isTwoAdditionalSteps() && action.getMotherNatureSteps()<=m.getCurrentCardPlayers().get(currentPlayer).getMothernaturesteps()){
            m.moveMotherNature(action.getMotherNatureSteps());
        }else{
            throw new IllegalArgumentException("the number of steps chosen is higher than the one indicated by the assistant card");
        }
        /*
         * if there isn't a no entry tile on the island check if it can be conquered and eventually conquer it
         * while if there is a no entry tile, do nothing and place the entry tile again on the character card
         */
        int noEntryCards=m.getIslandByPosition(m.getMotherNaturePosition()).getNoEntryCard();
        if(noEntryCards==0){
            Conquest c=m.computeInfluence(m.getMotherNaturePosition());
            m.setConquest(c);

        }
        else{
            m.setConquest(null);
            m.getIslandByPosition(m.getMotherNaturePosition()).setNoEntryCard(noEntryCards-1);
            int n=m.getCharactersPositions().get("herbalist.jpg");
            m.getCharacterCards().get(n).setNoEntryTiles(Optional.of(m.getCharacterCards().get(n).getNoEntryTiles().get()+1));
        }
        for(Player p: m.getPlayers())
        System.out.println("towers "+p.getNickname()+" "+ p.getNumberOfTower());

    }

}
