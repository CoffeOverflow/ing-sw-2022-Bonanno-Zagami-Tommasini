package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.CharacterCard;
import it.polimi.ingsw.Model.GameModel;

import java.util.Optional;

/**
 * @author Federica Tommasini
 * implementation of the state interface of the game controller
 */
public class PlayCharacterCardState implements GameControllerState{
    /**
     * play the character card chosen by the player
     * @param gc instance of the controller
     * @param action object containing parameters for the action
     * @throws IllegalStateException if the player doesn't have enough money to play the card
     */
    public void turnAction(GameController gc, Action action) throws IllegalStateException{
        GameModel model=gc.getModel();
        int cardPos=model.getCharactersPositions().get(action.getAsset());
        CharacterCard card=model.getCharacterCards().get(cardPos);
        if(model.getPlayerByID(model.getCurrentPlayer()).getMoney()>=card.getCost()) {
            if (action.getChosenColor() != null) {
                card.setChosenColor(Optional.of(action.getChosenColor()));
            }
            if (action.getChosenStudents() != null && action.getEntranceStudents() == null) {
                card.setChosenStudents(action.getChosenStudents());
            }
            if (action.getEntranceStudents() != null && action.getChosenStudents() != null ) {
                card.setChosenStudents(action.getChosenStudents(),action.getEntranceStudents());
            }
            card.useCard(action.getPosIsland(), model);
            if (!model.getFirstUseCharacters()[cardPos]) {
                model.setFirstUseCharacters(cardPos);
                card.increaseCost();
            }
        }else{
            throw new IllegalStateException("Not enough money");
        }

    }
}
