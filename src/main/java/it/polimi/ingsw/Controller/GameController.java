package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.State.GameControllerState;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Conquest;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameController {

    private GameControllerState state;

    private GameModel model;

    private Integer[] turnOrder;

    private Integer firstPlayer;

    private GameControllerState stateToReturn;

    private HashMap<Integer, AssistantCard> currentCardPlayers=new HashMap<>();

    private List<Player> winners=new ArrayList<Player>();

    public GameController(boolean expertMode,int numberOfPlayers){
        model=new GameModel(expertMode,numberOfPlayers);
        this.turnOrder=new Integer[numberOfPlayers];
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

    public synchronized GameModel getModel() {
        return model;
    }

    public void setModel(GameModel model) {
        this.model = model;
    }

    public Integer[] getTurnOrder() {
        return turnOrder;
    }

    public void setTurnOrder(int[] turnOrder) {

        for(int i=0; i< turnOrder.length;i++){
            this.turnOrder[i]=turnOrder[i];
        }
    }

    public void doAction(Action action){
        state.turnAction(this, action);
        model.printBag();
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
    }

    public List<Player> getWinners() {
        return winners;
    }

    public void setWinners(List<Player> winners) {
        this.winners = winners;
    }

    public GameControllerState getStateToReturn() {

        return stateToReturn;
    }

    public void setStateToReturn(GameControllerState stateToReturn) {

        this.stateToReturn = stateToReturn;
    }
}
