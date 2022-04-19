package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

/**
 * Player class
 * @author Angelo Zagami
 */
public class Player {

    private boolean gui;
    private String nickname;
    private EnumMap<Color, Integer> entryStudents = new EnumMap<>(Color.class);
    private EnumMap<Color, Integer> students = new EnumMap<>(Color.class);
    private Tower tower;
    private int numberOfTower;
    private int money;
    private ArrayList<AssistantCard> assistantCard = new ArrayList<AssistantCard>();
    private ArrayList<Color> professors = new ArrayList<Color>();

    /**
     * Create player and sets money to zero
     * @param nickname The nickname of the player
     * @param gui True is player has chosen gui mode, false otherwise
     * @param tower Color of towers of player
     * @param numberOfTower Number of towers that player can build
     * @param assistantCard List of assistant cards assigned to player
     */
    public Player(String nickname, boolean gui, Tower tower, int numberOfTower, ArrayList<AssistantCard> assistantCard){
        this.nickname = nickname;
        this.gui = gui;
        this.tower = tower;
        this.numberOfTower = numberOfTower;
        this.assistantCard = assistantCard;
        money = 0;
    }

    public boolean isGui() {
        return gui;
    }

    public String getNickname() {
        return nickname;
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

    public int getMoney() {
        return money;
    }

    /**
     *
     * @param entryStudents EnumMap<Color, Integer> contais student that have to be set in the entry of player's school
     */
    public void setEntryStudents(EnumMap<Color, Integer> entryStudents) {
        this.entryStudents = entryStudents;
    }

    /**
     *
     * @param entryStudents EnumMap<Color, Integer> contais student that have to be added in the entry of player's school
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

}
