package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Exceptions.MoneyException;
import it.polimi.ingsw.Model.CharacterCard;
import it.polimi.ingsw.Model.GameModel;

import java.util.Optional;

public class PlayCharacterCardState implements GameControllerState{

    public void turnAction(GameController gc, Action action){
        GameModel model=gc.getModel();
        int cardPos=model.getCharactersPositions().get(action.getAsset());
        CharacterCard card=model.getCharacterCards().get(cardPos);
        System.out.println("dentro state1");
        if(model.getPlayerByID(model.getCurrentPlayer()).getMoney()>=card.getCost()) {
            if (action.getChosenColor() != null) {
                System.out.println("dentro state2");
                card.setChosenColor(Optional.of(action.getChosenColor()));
            }
            if (action.getChosenStudents() != null && action.getEntranceStudents() == null) {
                System.out.println("dentro state3");
                card.setChosenStudents(action.getChosenStudents());
            }
            if (action.getEntranceStudents() != null && action.getChosenStudents() != null ) {
                System.out.println("dentro state4");
                card.setChosenStudents(action.getChosenStudents(),action.getEntranceStudents());
            }
            if (action.getChosenNumberOfSteps() != 0) {
                System.out.println("dentro state5");
                card.setChosenNumberOfSteps(Optional.of(action.getChosenNumberOfSteps()));
            }
            if (!model.getFirstUseCharacters()[cardPos]) {
                System.out.println("dentro state6");
                model.setFirstUseCharacters(cardPos);
                card.increaseCost();
            }
            System.out.println("dentro state7");
            System.out.println(action.getPosIsland());
            card.useCard(action.getPosIsland(), model);

        }else{
            throw new IllegalStateException();
        }

    }
}
