package it.polimi.ingsw.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * Player class
 * @author Angelo Zagami
 */
public class Player {

    private boolean expertMode;
    private String nickname;
    private int playerID;
    private EnumMap<Color, Integer> entryStudents = new EnumMap<>(Color.class);
    private EnumMap<Color, Integer> students = new EnumMap<>(Color.class);
    private Tower tower;
    private int numberOfTower;
    private int money;
    private ArrayList<AssistantCard> assistantCards;
    private ArrayList<Color> professors = new ArrayList<Color>();
    private Wizards wizard;

    /**
     * Create player and sets money to zero
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
        money = 0;
        //JSON Assistant card
        try {
            JsonReader json = new JsonReader(new FileReader("src/main/resources/json/assistants.json"));
            assistantCards = new Gson().fromJson(json, new TypeToken<List<AssistantCard>>(){}.getType());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for(Color c:Color.values())
            students.put(c,0);
    }



    public String getNickname() {
        return nickname;
    }

    public int getPlayerID(){
        return this.playerID;
    }

    public EnumMap<Color, Integer> getEntryStudents() {
        return entryStudents;
    }

    public EnumMap<Color, Integer> getStudents() {
        return students;
    }

    /**
     *
     * @param color Color of the student
     * @return The number of students in the player's school that have the color specified
     */
    public int getStudentsOf(Color color){
        return students.get(color);
    }

    public Tower getTower() {
        return tower;
    }

    public int getNumberOfTower() {
        return numberOfTower;
    }
    public void buildTower() {
        numberOfTower--;
    }
    public void setNumberOfTower(int numberOfTower) {
        this.numberOfTower = numberOfTower;
    }

    public int getMoney() {
        return money;
    }

    /**
     *
     * @param entryStudents EnumMap<Color, Integer> contains student that have to be set in the entry of player's school
     */
    public void setEntryStudents(EnumMap<Color, Integer> entryStudents) {
        this.entryStudents = entryStudents;
    }

    /**
     *
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
        if((students.get(color) % 3) == 0 && expertMode){
            money++;
        }
    }

    /**
     * Add professor of specified color to player's school
     * @param color
     */
    public void addProfessor(Color color){
        professors.add(color);
    }

    /**
     * Remove professor of specified color to player's school
     * @param color
     */
    public void removeProfessor(Color color){
        professors.remove(color);
    }

    /**
     * Check if a professor of specified color is present in player's school
     * @param color
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

    public void removeEntryStudent(Color color){
        entryStudents.merge(color, -1, Integer::sum);
    }

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

    public boolean studentIsPresent(Color color){
        return this.entryStudents.get(color) >= 1;
    }

    public ArrayList<AssistantCard> getAssistantCards(){
        return this.assistantCards;
    }

    public Wizards getWizard() {
        return wizard;
    }

    public void setWizard(Wizards wizard) {
        this.wizard = wizard;
    }

    public void decreaseMoney(int n){
        money-=n;
    }
}
