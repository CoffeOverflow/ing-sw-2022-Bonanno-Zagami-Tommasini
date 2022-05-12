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
    private Optional<Integer> noEntryTiles;



    /**
     * constructor for the character cards that have some students placed on them
     * @param asset name of the file corresponding to the card
     * @param students contains the student placed on the card, mapping their color with their quantity
     * @throws IllegalArgumentException if the number of students is wrong
     * @throws IllegalStateException if the card doesn't contain students
     */
    public CharacterCard( String asset, EnumMap<Color, Integer> students)
            throws IllegalArgumentException, IllegalStateException{

        this.asset = asset;
        int studentNumber=0;
        for(Color c: students.keySet()){
            studentNumber+=students.get(c);
        }

        switch (asset) {
            case "innkeeper.jpg":
                if (students!=null && studentNumber == 4) {
                    this.students=Optional.of(students);
                }else{
                    throw new IllegalArgumentException("the card must contain four students and the chosen student must be one");
                }
                effect = new Effect1();
                cost=1;
                break;
            case "clown.jpg":
                if (students!=null && studentNumber == 6){
                    this.students=Optional.of(students);
                }
                else {
                    throw new IllegalArgumentException("the card must contain six students and the chosen students must be at most three");
                }
                effect = new Effect6();
                cost=1;
                break;
            case "princess.jpg":
                if (students!=null && studentNumber == 4){
                    this.students=Optional.of(students);
                }
                else {
                    throw new IllegalArgumentException("the card must contain four students and the chosen student must be one");
                }
                effect = new Effect10();
                cost=2;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + asset);
        }
    }

    /**
     * constructor for the character cards that don't have students on them
     * @param asset name of the file corresponding to the card
     */
    public CharacterCard(String asset){
        this.asset = asset;
        switch(asset){
            case "auctioneer.jpg":
                cost=3;
                effect=new Effect2();
                break;
            case "postman.jpg":
                cost=1;
                effect=new Effect3();
                break;
            case "herbalist.jpg":
                noEntryTiles=Optional.of(4);
                cost=2;
                effect=new Effect4();
                break;
            case "centaur.jpg":
                cost=3;
                effect=new Effect5();
                break;
            case "infantryman.jpg":
                cost=2;
                effect=new Effect7();
                break;
            case "lumberjack.jpg":
                cost=3;
                effect=new Effect8();
                break;
            case "storyteller.jpg":
                cost=1;
                effect=new Effect9();
                break;
            case "thief.jpg":
                cost=3;
                effect=new Effect11();
                break;
            case "merchant.jpg":
                cost=2;
                effect=new Effect12();
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
        int studentNumber=0;
        for(Color c: chosenStudents.keySet()){
            studentNumber+=chosenStudents.get(c);
        }
        if( (this.asset.equals("innkeeper.jpg") && studentNumber==1) ||
                (this.asset.equals("clown.jpg") && studentNumber<=3) ||
                (this.asset.equals("princess.jpg") && studentNumber==1)
        )
            this.chosenStudents=Optional.of(chosenStudents);
        else{
            throw new IllegalStateException("Unexpected number of chosen students: " + studentNumber);
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
     * @param islandPosition island on which the changes will be applied
     * @param model instance of the game model
     */
    public void useCard(int islandPosition, GameModel model){
        Player player=model.getPlayerByID(model.getCurrentPlayer());
        effect.effect(player,islandPosition, model,this);
        if(chosenStudents.isPresent()){
            for(Color c: chosenStudents.get().keySet()){
                students.get().put(c,students.get().get(c)-chosenStudents.get().get(c));
                if(students.get().get(c)==0)
                    students.get().remove(c);
            }
            chosenStudents=null;
            //TODO trovare un modo per estrarne altri dalla bag (probabilmente va spostato nel metodo useCharacterCard del model
        }
        player.decreaseMoney(this.cost);
    }

    public void increaseCost(){
        cost++;
    }



}