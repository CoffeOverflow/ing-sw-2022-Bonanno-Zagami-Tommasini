package it.polimi.ingsw.Model;

public interface Effect {

    /**
     * execute the effect of the character card
     * @param player player who uses the character card
     * @param island island to which the changes will be applied
     * @param card character card that is used
     */
    void effect(Player player, Island island, CharacterCard card);
}

/**
 * interface Effect
 * @author Federica Tommasini
 */
class Effect1 implements Effect{

    /**
     * place one student, chosen by the player out of the four of the card, on the specified island
     * @param player player who uses the character card
     * @param island island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, Island island, CharacterCard card){//carta che ha 4 studenti e il giocatore ne sceglie uno da mettere su un'isola
        island.addStudents(card.chosenStudents.get().keySet().iterator().next(),1);
    }
}

class Effect2 implements Effect{

    /**
     * compute the influence on the island as if mother nature ended up on it
     * @param player player who uses the character card
     * @param island island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, Island island, CharacterCard card){
        int max=0;
        int n=0;
        int position;
        for(int i=0; i<5; i++) {
            n=island.getStudentsOf(Color.values()[i]);
            if(n>max){
                max=n;
                position=i;
            }
        }
        //TODO capire come posso vedere div'è il professore del colore massimo
        //if(island.getTower())
    }
}
class Effect3 implements Effect{

    /**
     * move mother nature at most of two additional steps with respect to the value of the assistant card
     * @param player player who uses the character card
     * @param island island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, Island island, CharacterCard card){

    }
}

class Effect4 implements Effect{

    /**
     * place a noEntryCard on the island, so that the first time that mother nature
     * ends up on it, the influence is not calculated
     * @param player player who uses the character card
     * @param island island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, Island island, CharacterCard card){
        island.setNoEntryCard(true);
    }
}

class Effect5 implements Effect{

    /**
     * during the computation of the influence of the island the number of towers is not considered
     * @param player player who uses the character card
     * @param island island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, Island island, CharacterCard card){

    }
}

class Effect6 implements Effect{

    /**
     * switch at most three students, among the six of the card, with students in the school entrance
     * @param player player who uses the character card
     * @param island island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, Island island, CharacterCard card){
        //TODO trovare un modo per passargli anche gli studenti da togliere dall'ingresso della scuola come parametro
    }
}
class Effect7 implements Effect{

    /**
     * assign two additional points to the player when computing the influence on the island
     * @param player player who uses the character card
     * @param island island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, Island island, CharacterCard card){

    }
}

class Effect8 implements Effect{

    /**
     * the player choose a color that is not considered in the computation of the influence
     * of the island in that turn
     * @param player player who uses the character card
     * @param island island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, Island island, CharacterCard card){

    }
}

class Effect9 implements Effect{

    /**
     * switch at most two students of the dining room with students of the entrance of the school
     * @param player player who uses the character card
     * @param island island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, Island island, CharacterCard card){

    }
}

class Effect10 implements Effect{

    /**
     * place the student chosen by the player from the four of the card on his dining room
     * @param player player who uses the character card
     * @param island island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, Island island, CharacterCard card){
        //int n=player.getStudentsOf(card.chosenStudents.get().keySet().iterator().next());
        //player.setStudents(card.chosenStudents.get().keySet().iterator().next(),n+1);
    }
}

class Effect11 implements Effect{

    /**
     * All the players put three students of the chosen color, placed in their dining room,
     * back into the bag
     * @param player player who uses the character card
     * @param island island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, Island island, CharacterCard card){

    }
}

class Effect12 implements Effect{

    /**
     * during the current turn, the player takes control of the professors even if he
     * has the same number of students in his dining room of the player who is currently
     * controlling such professors
     * @param player player who uses the character card
     * @param island island to which the changes will be applied
     * @param card character card that is used
     */
    public void effect(Player player, Island island, CharacterCard card){

    }
}