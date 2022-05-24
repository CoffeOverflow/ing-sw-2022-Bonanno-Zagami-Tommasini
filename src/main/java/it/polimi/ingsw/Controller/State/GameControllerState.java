package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Exceptions.MoneyException;

public interface GameControllerState {
    public void turnAction(GameController gc, Action action);
}
