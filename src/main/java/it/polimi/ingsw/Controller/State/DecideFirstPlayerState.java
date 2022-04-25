package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class DecideFirstPlayerState implements GameControllerState {
    @Override
    public void turnAction(GameController gc, Action action){

        Integer[] players=new Integer[gc.getModel().getNumberOfPlayers()];
        Integer[] values= new Integer[players.length];

        //fill the array in order from the first player and then clockwise
        for(int i=gc.getFirstPlayer(); i<gc.getModel().getNumberOfPlayers();i++) {
            players[i] = i;
            values[i]=action.getCurrentCardPlayers().get(players[i]).getValue();
        }
        for(int i=0; i<gc.getFirstPlayer(); i++) {
            players[i] = i;
            values[i]=action.getCurrentCardPlayers().get(players[i]).getValue();
        }

        //Check if the assistant card played are different
        //in case two are the same check if the second player who has played the card
        //has a different card to play in his deck
        for(int i=0; i<values.length;i++){
            for(int j=i+1; j<values.length; j++){
                if(values[i]==values[j]){
                   Player p=gc.getModel().getPlayerById();
                   ArrayList<AssistantCard> deck =p.getAssistantCards();
                   for(AssistantCard c : deck){
                       for(int k=0; k<values.length;k++) {
                           if (k != j && values[k] != c.getValue()) {
                               //DEVE RIGIOCARE LA CARTA
                               System.out.println("the player must use another assistant card");
                               //throw new IllegalArgumentException("the player must use another assistant card");
                               break;
                           }

                       }
                   }
                }

            }
        }

        gc.getModel().setCurrentCardPlayers(action.getCurrentCardPlayers());

        //determine order of action of the players in the turn
        for(int i=0; i<players.length; i++){//valore più basso, gioca prima
            for (int j = 0; j < players.length - i - 1; j++){
                if (values[j] > values[j + 1]) {
                    int tempV=values[j];
                    int temp = players[j];
                    values[j] = values[j + 1];
                    values[j + 1] = tempV;
                    players[j] = players[j + 1];
                    players[j + 1] = temp;
                }
            }
        }

        gc.setFirstPlayer(players[0]);
        gc.setTurnOrder(players);
    }


}
