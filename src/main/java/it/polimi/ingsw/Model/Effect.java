package it.polimi.ingsw.Model;

import it.polimi.ingsw.Controller.GameController;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * interface Effect
 * @author Federica Tommasini
 */
public interface Effect {

    /**
     * execute the effect of the character card
     * @param player player who uses the character card
     * @param islandPosition island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, int islandPosition, GameModel model, CharacterCard card);
}

class Effect1 implements Effect{

    /**
     * place one student, chosen by the player out of the four of the card, on the specified island
     * @param player player who uses the character card
     * @param islandPosition island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, int islandPosition, GameModel model, CharacterCard card){
        model.moveStudentsToIsland(islandPosition,card.getChosenStudents().get());
        EnumMap<Color,Integer> newStudents=model.getStudentsFromBag(1);
        card.getStudents().get().forEach((k, v) -> newStudents.merge(k, v, Integer::sum));
    }
}

class Effect2 implements Effect{

    /**
     * compute the influence on the island as if mother nature ended up on it
     * @param player player who uses the character card
     * @param islandPosition island to which the changes will be applied
     * @param card character card that is used
     */

    public void effect(Player player, int islandPosition,  GameModel model, CharacterCard card){
        int noEntryCards=model.getIslandByPosition(islandPosition).getNoEntryCard();
        if(noEntryCards==0)
            model.computeInfluence(islandPosition);
        else{
            model.getIslandByPosition(islandPosition).setNoEntryCard(noEntryCards-1);
            int n=model.getCharactersPositions().get("herbalist.jpg");
            model.getCharacterCards().get(n).setNoEntryTiles(Optional.of(model.getCharacterCards().get(n).getNoEntryTiles().get()+1));
        }

    }
}
class Effect3 implements Effect{

    /**
     * move mother nature at most of two additional steps with respect to the value of the assistant card
     * @param player player who uses the character card
     * @param islandPosition island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, int islandPosition, GameModel model, CharacterCard card){

        model.setTwoAdditionalSteps(true);

    }
}

class Effect4 implements Effect{

    /**
     * place a noEntryCard on the island, so that the first time that mother nature
     * ends up on it, the influence is not calculated
     * @param player player who uses the character card
     * @param islandPosition island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, int islandPosition, GameModel model,  CharacterCard card){
        int noEntryCards=model.getIslandByPosition(model.getMotherNaturePosition()).getNoEntryCard();
        model.getIslandByPosition(islandPosition).setNoEntryCard(noEntryCards+1);
        int n=model.getCharactersPositions().get("herbalist.jpg");
        model.getCharacterCards().get(n).setNoEntryTiles(Optional.of(model.getCharacterCards().get(n).getNoEntryTiles().get()-1));
    }
}

class Effect5 implements Effect{

    /**
     * during the computation of the influence of the island the number of towers is not considered
     * @param player player who uses the character card
     * @param islandPosition island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, int islandPosition, GameModel model,CharacterCard card){
        model.setTowersNotCounted(true);
    }
}

class Effect6 implements Effect{
    /**
     * switch at most three students, among the six of the card, with students in the school entrance
     * @param player player who uses the character card
     * @param islandPosition island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, int islandPosition, GameModel model, CharacterCard card){
        EnumMap<Color, Integer> cardStudents=card.getStudents().get();
        for(Color c: card.getChosenStudents().get().keySet()){
            int n= cardStudents.get(c);
            cardStudents.put(c,n-card.getChosenStudents().get().get(c));
        }
        cardStudents.putAll(card.getEntranceStudents().get());
        card.setStudents(cardStudents);
        player.addEntryStudents(card.getChosenStudents().get());
    }
}
class Effect7 implements Effect{

    /**
     * assign two additional points to the player when computing the influence on the island
     * @param player player who uses the character card
     * @param islandPosition island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, int islandPosition, GameModel model, CharacterCard card){
        model.setTwoAdditionalPoints(true);

    }
}

class Effect8 implements Effect{

    /**
     * the player choose a color that is not considered in the computation of the influence
     * of the island in that turn
     * @param player player who uses the character card
     * @param islandPosition island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, int islandPosition,GameModel model, CharacterCard card){
        model.setNotCountedColor(card.getChosenColor().get());
    }
}

class Effect9 implements Effect{

    /**
     * switch at most two students of the dining room with students of the entrance of the school
     * @param player player who uses the character card
     * @param islandPosition island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, int islandPosition, GameModel model, CharacterCard card){
        for(Color c: card.getEntranceStudents().get().keySet()){ //EntranceStudents:entrance students to swap
            int n= player.getEntryStudents().get(c);
            for(int i=0; i<n;i++)
                player.addStudentOf(c);
            player.getEntryStudents().put(c,n-card.getEntranceStudents().get().get(c));
        }
        for(Color c: card.getChosenStudents().get().keySet()){
            int n= player.getStudentsOf(c);
            player.setStudents(c,n-card.getChosenStudents().get().get(c));
        }
        player.addEntryStudents(card.getChosenStudents().get()); //ChosenStudents:dining room students to swap
    }
}

class Effect10 implements Effect{

    /**
     * place the student chosen by the player from the four of the card on his dining room
     * @param player player who uses the character card
     * @param islandPosition island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, int islandPosition, GameModel model,  CharacterCard card){
        player.addStudentOf((Color)card.getChosenStudents().get().keySet().toArray()[0]);
        EnumMap<Color,Integer> newStudents=model.getStudentsFromBag(1);
        card.getStudents().get().forEach((k, v) -> newStudents.merge(k, v, Integer::sum));
    }
}

class Effect11 implements Effect{

    /**
     * All the players put three students of the chosen color, placed in their dining room,
     * back into the bag
     * @param player player who uses the character card
     * @param islandPosition island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, int islandPosition, GameModel model,CharacterCard card){
        int count=0;
        for(int i=0; i<model.getNumberOfPlayers(); i++){
              count+=model.getPlayerByID(i).removeThreeStudentOf(card.getChosenColor().get());
        }
        model.addStudentsBag(card.getChosenColor().get(),count);

    }
}

class Effect12 implements Effect{

    /**
     * during the current turn, the player takes control of the professors even if he
     * has the same number of students in his dining room of the player who is currently
     * controlling such professors
     * @param player player who uses the character card
     * @param islandPosition island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, int islandPosition, GameModel model,CharacterCard card){
         for(Professor p:model.getProfessors().values()){
             try{
                if(p.getPlayer().getStudentsOf(p.getColor())==
                        player.getStudentsOf(p.getColor())){
                    p.goToSchool(player);
                 }
             }catch(NoSuchElementException e){

             }
         }

    }
}