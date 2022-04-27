package it.polimi.ingsw.Model;

import it.polimi.ingsw.Controller.GameController;

import java.util.EnumMap;
import java.util.Optional;

/**
 * class CharacterCard
 * @author Federica Tommasini
 */
public class CharacterCard {
    private int cost;
    private String asset;
    private Effect effect;
    private Optional<EnumMap<Color, Integer>> students;
    private Optional<EnumMap<Color, Integer>> chosenStudents;

    private Optional<EnumMap<Color, Integer>> entranceStudents;

    private Optional<Integer> chosenNumberOfSteps;
    private Optional<Color> chosenColor;



    /**
     * constructor for the character cards that have some students placed on them
     * @param cost cost that has to be paid to use the card
     * @param asset name of the file corresponding to the card
     * @param students contains the student placed on the card, mapping their color with their quantity
     * @throws IllegalArgumentException if the number of students is wrong
     * @throws IllegalStateException if the card doesn't contain students
     */
    public CharacterCard(int cost, String asset, EnumMap<Color, Integer> students)
            throws IllegalArgumentException, IllegalStateException{

        this.cost = cost;
        this.asset = asset;

        switch (asset) {
            case "CarteTOT_front":
                if (students!=null && students.size() == 4) {
                    this.students=Optional.of(students);
                }else{
                    throw new IllegalArgumentException("the card must contain four students and the chosen student must be one");
                }
                effect = new Effect1();
                break;
            case "CarteTOT_front6":
                if (students!=null && students.size() == 6){
                    this.students=Optional.of(students);
                }
                else {
                    throw new IllegalArgumentException("the card must contain six students and the chosen students must be at most three");
                }
                effect = new Effect6();
                break;
            case "CarteTOT_front10":
                if (students!=null && students.size() == 4){
                    this.students=Optional.of(students);
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

    public Optional<EnumMap<Color, Integer>> getChosenStudents() {
        return chosenStudents;
    }

    public void setChosenStudents(Optional<EnumMap<Color, Integer>> chosenStudents) {
        this.chosenStudents = chosenStudents;
    }
    public void setChosenNumberOfSteps(Optional<Integer> chosenNumberOfSteps) {
        this.chosenNumberOfSteps = chosenNumberOfSteps;
    }

    public Optional<Integer> getChosenNumberOfSteps() {
        return chosenNumberOfSteps;
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

    public Optional<EnumMap<Color, Integer>> getEntranceStudents() {
        return entranceStudents;
    }

    public void setEntranceStudents(Optional<EnumMap<Color, Integer>> entranceStudents) {
        this.entranceStudents = entranceStudents;
    }

    public int getCost(){
        return cost;
    }

    public String getAsset(){
        return asset;
    }

    public void setStudents(EnumMap<Color, Integer> students) {
        this.students=Optional.of(students);
    }

    public Optional<EnumMap<Color, Integer>> getStudents() {
        return students;
    }

    /**
     * set the students that the player chose to take from the character card
     * @param chosenStudents students taken from the character card
     * @throws IllegalStateException if the card doesn't contain students or if the number of chosen student is wrong
     */
    public void setChosenStudents(EnumMap<Color, Integer> chosenStudents) throws IllegalStateException{
        if( (this.asset.equals("CarteTOT_front1") && chosenStudents.size()==1) ||
            (this.asset.equals("CarteTOT_front6") && chosenStudents.size()<=3) ||
            (this.asset.equals("CarteTOT_front10") && chosenStudents.size()==1)
        )
        this.chosenStudents=Optional.of(chosenStudents);
        else{
            throw new IllegalStateException("Unexpected number of chosen students: " + chosenStudents.size());
        }
        //TODO aggiungere per carte da swappare
    }

    public Optional<Color> getChosenColor() {
        return chosenColor;
    }

    public void setChosenColor(Optional<Color> chosenColor) {
        this.chosenColor = chosenColor;
    }

    /**
     * execute the effect of the character card
     * @param player player who uses the card
     * @param islandPosition island on which the changes will be applied
     */
    public void useCard(Player player, int islandPosition, GameModel model){
        effect.effect(player,islandPosition, model,this);
        if(chosenStudents.isPresent()){
            //TODO qua devo togliere gli studenti scelti dalla carta
            chosenStudents.empty();
            //TODO trovare un modo per estrarne altri dalla bag (probabilmente va spostato nel metodo useCharacterCard del model
        }
    }



}
