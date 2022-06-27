package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Exceptions.MoneyException;

public interface GameControllerState {
    /**
     * execute the action of the current game phase
     * @param gc instance of the controller
     * @param action object containing parameters for the action
     */
    public void turnAction(GameController gc, Action action);
}
