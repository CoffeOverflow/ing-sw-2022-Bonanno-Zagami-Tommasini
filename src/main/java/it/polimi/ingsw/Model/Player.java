package it.polimi.ingsw.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;


/**
 * Player class
 * @author Angelo Zagami
 */
public class Player {

    private final boolean expertMode;
    private final String nickname;
    private final int playerID;
    private EnumMap<Color, Integer> entryStudents = new EnumMap<>(Color.class);
    private EnumMap<Color, Integer> students = new EnumMap<>(Color.class);
    private final Tower tower;
    private int numberOfTower;
    private int money;
    private final ArrayList<AssistantCard> assistantCards;
    private final ArrayList<Color> professors = new ArrayList<>();
    private Wizards wizard;

    /**
     * Create player and sets money to zero, reads the assistant cards from json file
     * @param nickname The nickname of the player
     * @param expertMode True is the game is expert mode
     * @param tower Color of towers of player
     * @param numberOfTower Number of towers that player can build
     */
    public Player(int playerID,String nickname, boolean expertMode, Tower tower, int numberOfTower){
        this.playerID = playerID;
        this.nickname = nickname;
        this.expertMode = expertMode;
        this.tower = tower;
        this.numberOfTower = numberOfTower;
        money = expertMode ? 1:0;
        //JSON Assistant card
        InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/json/assistants.json"));
        JsonReader json = new JsonReader(reader);
        assistantCards = new Gson().fromJson(json, new TypeToken<List<AssistantCard>>(){}.getType());
        for(Color c:Color.values())
            students.put(c,0);
    }


    /***
     * Get the player nickname
     * @return The nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /***
     * Get the player ID
     * @return The player ID
     */
    public int getPlayerID(){
        return this.playerID;
    }

    /***
     * Get the student on the player's entrance
     * @return EnumMap with entrance student of the player
     */
    public EnumMap<Color, Integer> getEntryStudents() {
        return entryStudents;
    }

    /***
     * Get the student on the player's school
     * @return EnumMap with school student of the player
     */
    public EnumMap<Color, Integer> getStudents() {
        return students;
    }

    /**
     * Get the number of student in player school of specified color
     * @param color Color of the student
     * @return The number of students in the player's school that have the color specified
     */
    public int getStudentsOf(Color color){
        return students.get(color);
    }

    /***
     * Get the Tower of the player
     * @return The player's Tower
     */
    public Tower getTower() {
        return tower;
    }

    /***
     * Get the number of player's tower, the towers that haven't built yet
     * @return The number of olayer towers
     */
    public int getNumberOfTower() {
        return numberOfTower;
    }

    /***
     * Build the number of specified tower on the islands
     * @param num Number pof tower to build
     */
    public void buildTower(int num) {
        numberOfTower-=num;
    }

    /***
     * Set the player towers
     * @param numberOfTower The number of towers
     */
    public void setNumberOfTower(int numberOfTower) {
        this.numberOfTower = numberOfTower;
    }

    /***
     * Get player's money
     * @return The number of money
     */
    public int getMoney() {
        return money;
    }

    /**
     * Set the entry student of player
     * @param entryStudents EnumMap<Color, Integer> contains student that have to be set in the entry of player's school
     */
    public void setEntryStudents(EnumMap<Color, Integer> entryStudents) {
        this.entryStudents = entryStudents;
    }

    /**
     * Add entry student to player
     * @param entryStudents EnumMap<Color, Integer> contains student that have to be added in the entry of player's school
     */
    public void addEntryStudents(EnumMap<Color, Integer> entryStudents){
        entryStudents.forEach((k, v) -> this.entryStudents.merge(k, v, Integer::sum));
    }

    /**
     * Set students of specified color in player's school
     * @param color Color of the students
     * @param number Number of students
     */
    public void setStudents(Color color, int number){
        students.put(color, number);
    }

    /**
     * Add one student of specified color in player's school
     * @param color Color of student
     */
    public void addStudentOf(Color color){
        students.merge(color, 1, Integer::sum);
    }

    /**
     * Add professor of specified color to player's school
     * @param color The color of professor
     */
    public void addProfessor(Color color){
        professors.add(color);
    }

    /**
     * Remove professor of specified color to player's school
     * @param color The color of professor
     */
    public void removeProfessor(Color color){
        professors.remove(color);
    }

    /**
     * Check if a professor of specified color is present in player's school
     * @param color The color of professor
     * @return True if professor is present, false otherwise
     */
    public boolean isPresentProfessor(Color color){
        return professors.contains(color);
    }


    public void useAssistantCard(String name){
        for(AssistantCard card: assistantCards){
            if (card.getName().equals(name)){
                assistantCards.remove(card);
                break;
            }
        }
    }

    /***
     * Remove one entry student of the specified color
     * @param color The color of the student to remove
     */
    public void removeEntryStudent(Color color){
        entryStudents.merge(color, -1, Integer::sum);
    }

    /***
     *
     * @param color
     * @return
     */
    public int removeThreeStudentOf(Color color){
        int res=0;
        int n=getStudentsOf(color);
        if(n>=3){
            students.put(color,n-3);
            res=3;
        }else{
            students.put(color,0);
            res=n;
        }
        return res;
    }

    /***
     * Check if a student of specifeid color is present in the entrance
     * @param color Color of the students
     * @return True if the student is present, false otherwise
     */
    public boolean studentIsPresent(Color color){
        return this.entryStudents.get(color) >= 1;
    }

    /***
     * Return the player's AssistantCards
     * @return List of AssistantCard
     */
    public ArrayList<AssistantCard> getAssistantCards(){
        return this.assistantCards;
    }

    /***
     * Get the player Wizard
     * @return Player's Wizard
     */
    public Wizards getWizard() {
        return wizard;
    }

    /***
     * Set the player Wizard
     * @param wizard Wizard to set
     */
    public void setWizard(Wizards wizard) {
        this.wizard = wizard;
    }

    /***
     * Decrease player's money of specified value
     * @param n Number of Money
     */
    public void decreaseMoney(int n){
        money-=n;
    }

    /***
     * Add 1 money to the player
     */
    public void addMoney(){
        money++;
    }
}
