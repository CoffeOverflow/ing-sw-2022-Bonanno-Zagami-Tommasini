package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.State.GameControllerState;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.GameModel;

import java.util.HashMap;

public class GameController {

    private GameControllerState state;

    private GameModel model;

    private Integer[] turnOrder;

    private Integer firstPlayer;

    private HashMap<Integer, AssistantCard> currentCardPlayers=new HashMap<>();

    public GameController(){


    }

    public HashMap<Integer, AssistantCard> getCurrentCardPlayers() {
        return currentCardPlayers;
    }
    public void addCurrentAssistantCard(int player,AssistantCard card){
        currentCardPlayers.put(player,card);
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
        if(model.getPlayerByID(model.getCurrentPlayer()).getNumberOfTower() == 0)
            return true;
        return false;
    }

    public boolean fillCloud(){
        return model.getStudentsFromBag();
        //Manca controllo per saltare lo stato
    }
}
