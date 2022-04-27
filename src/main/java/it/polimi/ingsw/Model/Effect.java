package it.polimi.ingsw.Model;

import it.polimi.ingsw.Controller.GameController;

import java.util.EnumMap;
import java.util.HashMap;
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
        else model.getIslandByPosition(islandPosition).setNoEntryCard(noEntryCards-1);
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
        if(card.getChosenNumberOfSteps().get()<=model.getCurrentCardPlayers().get(model.getCurrentPlayer()).getMothernatureSteps()+2){
            model.moveMotherNature(card.getChosenNumberOfSteps().get());
        }else{
            System.out.println("the number of steps chosen is higher than the one indicated by the assistant card");
        }
        int noEntryCards=model.getIslandByPosition(islandPosition).getNoEntryCard();
        if(noEntryCards==0)
            model.computeInfluence(model.getMotherNaturePosition());
        else model.getIslandByPosition(islandPosition).setNoEntryCard(noEntryCards-1);

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
        //take control of the island:
        int key=0;
        HashMap<Integer, Integer> influences=new HashMap<>();
        Optional<Integer> conqueror=null;
        for(Integer p : model.getCurrentCardPlayers().keySet()){
            influences.put(p,model.getPlayerInfluence(p,islandPosition));
            if(influences.get(p)>key){
                key=model.getPlayerInfluence(p,islandPosition);
                conqueror=Optional.of(p);
            }
        }
        //check if the higher value of influence is unique
        if(conqueror.isPresent()){
            for(Integer p : model.getCurrentCardPlayers().keySet()){
                if(p!=conqueror.get() && influences.get(p)==influences.get(conqueror)){
                    conqueror=Optional.empty();
                }
            }
        }

        //if the value is unique, conquer the island
        if(conqueror.isPresent()){
            Optional<Tower> oldTower= model.getTowerOnIsland(islandPosition);
            model.setTowerOnIsland(islandPosition,conqueror.get());
            if(oldTower.isPresent()){
                int oldNumberOfTower=model.getPlayerByTower(oldTower.get()).getNumberOfTower();
                model.getPlayerByTower(oldTower.get()).setNumberOfTower(oldNumberOfTower+1);
            }
            model.getPlayerByID(conqueror.get()).buildTower();

            model.checkMergeIsland(islandPosition,
                    model.getPlayerTower(conqueror.get()));
        }
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
        //take control of the island:
        int key=0;
        HashMap<Integer, Integer> influences=new HashMap<>();
        Optional<Integer> conqueror=null;
        for(Integer p : model.getCurrentCardPlayers().keySet()){
            if(model.getTowerOnIsland(islandPosition).isPresent() &&
                    model.getTowerOnIsland(islandPosition).get().equals(model.getPlayerTower(p))){
                influences.put(p,model.getPlayerInfluence(p,islandPosition)
                        + model.getIslandByPosition(islandPosition).getNumberOfTowers());
            }else{
                influences.put(p,model.getPlayerInfluence(p,islandPosition));
            }
            if(p.equals(player)){
                influences.put(p,influences.get(p)+2);
            }
            if(influences.get(p)>key){
                key=model.getPlayerInfluence(p,islandPosition);
                conqueror=Optional.of(p);
            }
        }
        //check if the higher value of influence is unique
        if(conqueror.isPresent()){
            for(Integer p : model.getCurrentCardPlayers().keySet()){
                if(p!=conqueror.get() && influences.get(p)==influences.get(conqueror)){
                    conqueror=Optional.empty();
                }
            }
        }

        //if the value is unique, conquer the island
        if(conqueror.isPresent()){
            Optional<Tower> oldTower= model.getTowerOnIsland(islandPosition);
            model.setTowerOnIsland(islandPosition,conqueror.get());
            if(oldTower.isPresent()){
                int oldNumberOfTower=model.getPlayerByTower(oldTower.get()).getNumberOfTower();
                model.getPlayerByTower(oldTower.get()).setNumberOfTower(oldNumberOfTower+1);
            }
            model.getPlayerByID(conqueror.get()).buildTower();

            model.checkMergeIsland(islandPosition,
                    model.getPlayerTower(conqueror.get()));
        }

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
        //take control of the island:
        int key=0;
        HashMap<Integer, Integer> influences=new HashMap<>();
        Optional<Integer> conqueror=null;
        for(Integer p : model.getCurrentCardPlayers().keySet()){
            int influence = 0;
            for (Color c : Color.values()) {
                if(!c.equals(card.getChosenColor().get())){
                    if (model.getPlayerByID(p).equals(model.getProfessors().get(c).getPlayer())) {
                        influence += model.getIslandByPosition(islandPosition).getStudentsOf(c);
                    }
                }
            }
            if(model.getTowerOnIsland(islandPosition).isPresent() &&
                    model.getTowerOnIsland(islandPosition).get().equals(model.getPlayerTower(p))){
                influences.put(p,influence + model.getIslandByPosition(islandPosition).getNumberOfTowers());
            }else{
                influences.put(p,influence);
            }

            if(influences.get(p)>key){
                key=model.getPlayerInfluence(p,islandPosition);
                conqueror=Optional.of(p);
            }
        }
        //check if the higher value of influence is unique
        if(conqueror.isPresent()){
            for(Integer p : model.getCurrentCardPlayers().keySet()){
                if(p!=conqueror.get() && influences.get(p)==influences.get(conqueror)){
                    conqueror=Optional.empty();
                }
            }
        }

        //if the value is unique, conquer the island
        if(conqueror.isPresent()){
            Optional<Tower> oldTower= model.getTowerOnIsland(islandPosition);
            model.setTowerOnIsland(islandPosition,conqueror.get());
            if(oldTower.isPresent()){
                int oldNumberOfTower=model.getPlayerByTower(oldTower.get()).getNumberOfTower();
                model.getPlayerByTower(oldTower.get()).setNumberOfTower(oldNumberOfTower+1);
            }
            model.getPlayerByID(conqueror.get()).buildTower();

            model.checkMergeIsland(islandPosition,
                    model.getPlayerTower(conqueror.get()));
        }
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
        //int n=player.getStudentsOf(card.chosenStudents.get().keySet().iterator().next());
        //player.setStudents(card.chosenStudents.get().keySet().iterator().next(),n+1);
        //TODO
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
          //TODO
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
         //TODO
    }
}