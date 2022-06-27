package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Exceptions.MoneyException;
import it.polimi.ingsw.Model.CharacterCard;
import it.polimi.ingsw.Model.GameModel;

import java.util.Optional;

public class PlayCharacterCardState implements GameControllerState{

    public void turnAction(GameController gc, Action action) throws IllegalStateException{
        GameModel model=gc.getModel();
        int cardPos=model.getCharactersPositions().get(action.getAsset());
        CharacterCard card=model.getCharacterCards().get(cardPos);
        System.out.print("money: ");
        System.out.println(model.getPlayerByID(model.getCurrentPlayer()).getMoney());
        System.out.print("asset: "+card.getAsset() +"\n");
        System.out.println("cost: "+card.getCost());
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
            if (action.getChosenNumberOfSteps() != 0) {
                card.setChosenNumberOfSteps(Optional.of(action.getChosenNumberOfSteps()));
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
