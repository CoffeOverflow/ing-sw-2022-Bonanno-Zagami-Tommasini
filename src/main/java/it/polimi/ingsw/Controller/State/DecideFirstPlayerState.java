package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class DecideFirstPlayerState implements GameControllerState {
    @Override
    public void turnAction(GameController gc, Action action) throws IllegalArgumentException{

        Integer[] players=new Integer[gc.getModel().getNumberOfPlayers()];
        Integer[] values= new Integer[players.length];

        //fill the array in order from the first player and then clockwise
        /*int key=gc.getFirstPlayer();
        for(int i=0; i<gc.getModel().getNumberOfPlayers();i++) {
            players[i] = key;
            if(key+1<gc.getFirstPlayer())
            values[i]=gc.getCurrentCardPlayers().get(players[i]).getValue();
        }
        for(int i=0; i<gc.getFirstPlayer(); i++) {
            players[i] = i;
            values[i]=gc.getCurrentCardPlayers().get(players[i]).getValue();
        }*/

        int firstPosition=0;
        for(int i=0; i<gc.getModel().getPlayers().size(); i++){
            if(gc.getModel().getPlayers().get(i).getPlayerID()==gc.getFirstPlayer()) {
                firstPosition = i;
                break;
            }
        }
        int count=firstPosition;
        for(int i=0; i<gc.getModel().getNumberOfPlayers();i++){
            players[i]=gc.getModel().getPlayers().get(count).getPlayerID();
            values[i]=gc.getCurrentCardPlayers().get(players[i]).getValue();
            if(count<gc.getModel().getPlayers().size()-1)
                count++;
            else count=0;
        }

        //Check if the assistant card played are different
        //in case two are the same check if the second player who has played the card
        //has a different card to play in his deck
        for(int i=0; i<values.length;i++){
            for(int j=i+1; j<values.length; j++){
                if(values[i].equals(values[j])){
                   Player p=gc.getModel().getPlayerByID(players[j]);
                   ArrayList<AssistantCard> deck =p.getAssistantCards();
                   for(AssistantCard c : deck){
                       for(int k=0; k<values.length;k++) {
                           if (k != j && values[k] != c.getValue()) {
                               throw new IllegalArgumentException("the player must use another assistant card");
                           }

                       }
                   }
                }

            }
        }


        gc.getModel().setCurrentCardPlayers((HashMap<Integer, AssistantCard>)gc.getCurrentCardPlayers().clone());
        String[] cards ={"turtle","elephant","dog", "octopus","lizard", "fox", "eagle","cat","turkey","lion"};
        for(int i=0; i<players.length;i++){
            gc.getModel().useAssistantCard(players[i],cards[values[i]-1]);
        }

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
        gc.getCurrentCardPlayers().clear();
    }


}
