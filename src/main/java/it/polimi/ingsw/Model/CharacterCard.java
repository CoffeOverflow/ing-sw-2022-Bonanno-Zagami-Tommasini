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

   // private Optional<Integer> chosenNumberOfSteps;
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


    /**
     * @return the chosen students associated with the card
     */
    public Optional<EnumMap<Color, Integer>> getChosenStudents() {
        return chosenStudents;
    }

    /**
     * @return the students from the entrance to swap
     */
    public Optional<EnumMap<Color, Integer>> getEntranceStudents() {
        return entranceStudents;
    }

    /**
     * @return the cost of the card
     */
    public int getCost(){
        return cost;
    }

    /**
     * @return the asset associated to the card
     */
    public String getAsset(){
        return asset;
    }

    /**
     * method that takes new students from the bag and place them on the card
     * @param studentsToAdd students to add
     */
    public void addStudents(EnumMap<Color, Integer> studentsToAdd){
        studentsToAdd.forEach((k, v) -> this.students.get().merge(k, v, Integer::sum));
    }

    /**
     * method that sets the student to place on the card
     * @param students students to place
     */
    public void setStudents(EnumMap<Color, Integer> students) {
        this.students=Optional.of(students);
    }

    /**
     * @return the students placed on the card
     */
    public Optional<EnumMap<Color, Integer>> getStudents() {
        return students;
    }

    /**
     * set the students that the player chose to take from the character card
     * @param chosenStudents students taken from the character card
     * @throws IllegalStateException if the card doesn't contain students or if the number of chosen students is wrong
     */
    public void setChosenStudents(EnumMap<Color, Integer> chosenStudents) throws IllegalStateException{
        int studentNumber=0;
        for(Color c: chosenStudents.keySet()){
            studentNumber+=chosenStudents.get(c);
        }
        if( (this.asset.equals("innkeeper.jpg") && studentNumber==1) ||
                (this.asset.equals("princess.jpg") && studentNumber==1)
        )
            this.chosenStudents=Optional.of(chosenStudents);
        else{
            throw new IllegalStateException("Unexpected number of chosen students");
        }
    }

    /**
     * set the students associated with the effect on the card in the case in which also some from the entrance
     * of the player's school have to be selected
     * @param chosenStudents students to take from the card or from the school's dining hall (in the case of "storyteller" card)
     * @param entranceStudents students to take from the school's entrance
     * @throws IllegalStateException if if the card doesn't contain students or if the number of chosen/entrance students is wrong
     */
    public void setChosenStudents(EnumMap<Color, Integer> chosenStudents,EnumMap<Color, Integer> entranceStudents)throws IllegalStateException{
        int studentNumber=0;
        int entranceNumber=0;
        for(Color c: chosenStudents.keySet()){
            studentNumber+=chosenStudents.get(c);
        }
        for(Color c: entranceStudents.keySet()){
            entranceNumber+=entranceStudents.get(c);
        }
        if( ((this.asset.equals("clown.jpg") && studentNumber<=3)
                || (this.asset.equals("storyteller.jpg") && studentNumber<=2))
                && entranceNumber==studentNumber) {
            this.chosenStudents = Optional.of(chosenStudents);
            this.entranceStudents=Optional.of(entranceStudents);
        }else{
            throw new IllegalStateException("Unexpected number of chosen students");
        }

    }

    /**
     * @return the color chosen for the effect of the card
     */
    public Optional<Color> getChosenColor() {
        return chosenColor;
    }

    /**
     * associated with the cards "thief" and "lumberjack"
     * @param chosenColor color that has to be considered for the effect
     */
    public void setChosenColor(Optional<Color> chosenColor) {
        this.chosenColor = chosenColor;
    }

    /**
     * execute the effect of the character card
     * @param islandPosition island on which the changes will be applied
     * @param model instance of the game model
     */
    public void useCard(Integer islandPosition, GameModel model){
        Player player=model.getPlayerByID(model.getCurrentPlayer());
        effect.effect(player,islandPosition, model,this);
        int count=0;
        if(chosenStudents!=null && chosenStudents.isPresent() && this.getStudents()!=null &&  this.getStudents().isPresent()) {
            for(Color c: chosenStudents.get().keySet()){
                this.getStudents().get().put(c, this.getStudents().get().get(c)-chosenStudents.get().get(c));
            }
            chosenStudents = null;
        }
        if(entranceStudents!=null && entranceStudents.isPresent())
            entranceStudents=null;
        if(chosenColor!=null && chosenColor.isPresent())
            chosenColor=null;
        if(noEntryTiles!=null && noEntryTiles.isPresent())
            noEntryTiles=Optional.of(noEntryTiles.get()-1);

        player.decreaseMoney(this.cost);
    }

    /**
     * method that increases the cost of the card after its first usage
     */
    public void increaseCost(){
        cost++;
    }

    /**
     * @return the number of no-entry tiles placed on the "herbalist" card
     */
    public Optional<Integer> getNoEntryTiles() {
        return noEntryTiles;
    }

    /**
     * @param noEntryTiles number of no-entry tiles placed on the "herbalist" card
     */
    public void setNoEntryTiles(Optional<Integer> noEntryTiles) {
        this.noEntryTiles = noEntryTiles;
    }

}