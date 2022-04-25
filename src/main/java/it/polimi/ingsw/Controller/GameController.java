package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.State.GameControllerState;
import it.polimi.ingsw.Model.GameModel;

public class GameController {

    private GameControllerState state;

    private GameModel model;

    private Integer[] turnOrder;

    private Integer firstPlayer;

    public GameController(){


    }

    public Integer getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Integer firstPlayer) {
        this.firstPlayer = firstPlayer;
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

    public Integer[] getTurnOrder() {
        return turnOrder;
    }

    public void setTurnOrder(Integer[] turnOrder) {
        this.turnOrder = turnOrder;
    }

    public void doAction(Action action){
        state.turnAction(this, action);
    }

    public boolean checkEndGame(){
        if(model.getIslandSize() == model.getNumberOfPlayers())
            return true;
        //Manca controllo torri
        return false;
    }

    public boolean fillCloud(){
        return model.getStudentsFromBag();
        //Manca controllo per saltare lo stato
    }
}
