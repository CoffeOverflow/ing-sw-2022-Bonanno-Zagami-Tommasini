package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Federica Tommasini
 * implementation of the state interface of the game controller
 */
public class DecideFirstPlayerState implements GameControllerState {
    /**
     * takes the assistant cards played by the players in that turn and defines the turn order of the action phase
     * @param gc instance of the controller
     * @param action object containing parameters for the action
     * @throws IllegalArgumentException if a player plays a card already played by another player
     */
    @Override
    public void turnAction(GameController gc, Action action) throws IllegalArgumentException{

        /*
         * put in players the IDs of the players in the order in which they played the cards
         * put in values in order the values of the assistant cards played during the turn
         */
        int[] players=new int[gc.getModel().getNumberOfPlayers()];
        int[] values= new int[players.length];

        int firstPosition=0;
        for(int i=0; i<gc.getModel().getPlayers().size(); i++){
            if(gc.getModel().getPlayers().get(i).getPlayerID()==gc.getFirstPlayer()) {
                firstPosition = i;
                break;
            }
        }
        int count=firstPosition;
        for(int i=0; i<gc.getCurrentCardPlayers().size();i++){
            players[i]=gc.getModel().getPlayers().get(count).getPlayerID();
            values[i]=gc.getCurrentCardPlayers().get(players[i]).getValue();
            if(count<gc.getModel().getPlayers().size()-1)
                count++;
            else count=0;
        }

        /*
         * Check if the assistant cards played are different
         * in case two are the same check if the second player who has played the card has a different card
         * to play in his deck
         */
        for(int i=0; i<values.length;i++){
            for(int j=i+1; j<values.length; j++){
                if(values[i]!=0 && values[i]==(values[j])){
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

        /*
         * if all the players played their cards, determine their order of action in the turn
         * the one that played the card with the lower value goes first
         */
        if(gc.getCurrentCardPlayers().size()==gc.getModel().getNumberOfPlayers()) {
            Player p=gc.getModel().getPlayerByID(players[0]);
            if(p.getAssistantCards().isEmpty())
                gc.getModel().setLastRound(true);
            gc.getModel().setCurrentCardPlayers((HashMap<Integer, AssistantCard>) gc.getCurrentCardPlayers().clone());
            String[] cards = {"Turtle", "Elephant", "Dog", "Octopus", "Lizard", "Fox", "Eagle", "Cat", "Turkey", "Lion"};
            for (int i = 0; i < players.length; i++) {
                gc.getModel().useAssistantCard(players[i], cards[values[i] - 1]);
            }

            for (int i = 0; i < players.length; i++) {
                for (int j = 0; j < players.length - i - 1; j++) {
                    if (values[j] > values[j + 1]) {
                        int tempV = values[j];
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


}
