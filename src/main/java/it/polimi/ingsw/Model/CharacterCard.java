package it.polimi.ingsw.Model;

import java.util.EnumMap;
import java.util.Optional;

/**
 * class CharacterCard
 * @author Federica Tommasini
 */
public class CharacterCard {
    int cost;
    String asset;
    Effect effect;
    Optional<EnumMap<Color, Integer>> students;
    Optional<EnumMap<Color, Integer>> chosenStudents;

    /**
     * constructor for the character cards that have some students placed on them
     * @param cost cost that has to be paid to use the card
     * @param asset name of the file corresponding to the card
     * @param students contains the student placed on the card, mapping their color with their quantity
     * @throws IllegalArgumentException if the number of students is wrong
     * @throws IllegalStateException if the card doesn't contain students
     */
    public CharacterCard(int cost, String asset, EnumMap<Color, Integer> students) {

        this.cost = cost;
        this.asset = asset;

        switch (asset) {
            case "CarteTOT_front": //carta che ha 4 studenti e il giocatore ne sceglie uno da mettere su un'isola
                if (students!=null && students.size() == 4) {
                    this.students.of(students);
                }else{
                    throw new IllegalArgumentException("the card must contain four students and the chosen student must be one");
                }
                effect = new Effect1();
                break;
            case "CarteTOT_front6": //carta che ha 6 studenti, il giocatore ne può prendere al max 3 e scambiarli con 3 presenti nel suo ingresso
                if (students!=null && students.size() == 6){
                    this.students.of(students);
                }
                else {
                    throw new IllegalArgumentException("the card must contain six students and the chosen students must be at most three");
                }
                effect = new Effect6();
                break;
            case "CarteTOT_front10": //carta che ha 4 studenti, il giocatore ne può scegliere uno da mettere nella sua sala
                if (students!=null && students.size() == 4){
                    this.students.of(students);
                }
                else {
                    throw new IllegalArgumentException("the card must contain four students and the chosen student must be one");
                }
                effect = new Effect10();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + asset);
        }
    }

    /**
     * constructor for the character cards that don't have students on them
     * @param cost cost cost that has to be paid to use the card
     * @param asset name of the file corresponding to the card
     */
    public CharacterCard(int cost, String asset){
        this.cost = cost;
        this.asset = asset;
    }

    public int getCost(){
        return cost;
    }

    public String getAsset(){
        return asset;
    }


    public void setStudents(EnumMap<Color, Integer> students) {
        this.students.of(students);
    }

    /**
     * set the students that the player chose to take from the character card
     * @param chosenStudents students taken from the character card
     * @throws IllegalStateException if the card doesn't contain students or if the number of chosen student is wrong
     */
    public void setChosenStudents(EnumMap<Color, Integer> chosenStudents) {
        if( (this.asset.equals("CarteTOT_front1") && chosenStudents.size()==1) ||
            (this.asset.equals("CarteTOT_front6") && chosenStudents.size()<=3) ||
            (this.asset.equals("CarteTOT_front10") && chosenStudents.size()==1)
        )
        this.chosenStudents.of(chosenStudents);
        else{
            throw new IllegalStateException("Unexpected number of chosen students: " + chosenStudents.size());
        }
    }

    /**
     * execute the effect of the character card
     * @param player player who uses the card
     * @param island island on which the changes will be applied
     */
    public void useCard(Player player, Island island){
        effect.effect(player,island, this);
        if(chosenStudents.isPresent()){
            //TODO qua devo togliere gli studenti scelti dalla carta
            chosenStudents.empty();
            //TODO trovare un modo per estrarne altri dalla bag (probabilmente va spostato nel metodo useCharacterCard del model
        }
    }



}
