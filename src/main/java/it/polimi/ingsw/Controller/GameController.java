package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.State.GameControllerState;
import it.polimi.ingsw.Model.GameModel;

public class GameController {

    private GameControllerState state;

    private GameModel model;

    public GameController(){


    }

    public GameControllerState getState() {
        return state;
    }

    public void setState(GameControllerState state) {
        this.state = state;
    }

    public GameModel getModel() {
        return model;
    }

    public void setModel(GameModel model) {
        this.model = model;
    }

    public void doAction(Action action){
        state.turnAction(this, action);
    }
}
