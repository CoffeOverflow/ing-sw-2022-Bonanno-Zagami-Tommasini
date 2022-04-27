package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Optional;

/** Island class
 * @author Federica Tommasini
 */
public class Island {

    private Optional<Tower> tower;
    private int numberOfTowers;
    private EnumMap<Color,Integer> students;

    private int noEntryCards;

    /**
     * create an island
     */
    public Island(){
        students= new EnumMap<>(Color.class);
        students.put(Color.PINK,0);
        students.put(Color.RED,0);
        students.put(Color.YELLOW,0);
        students.put(Color.BLUE,0);
        students.put(Color.GREEN,0);
        numberOfTowers=0;
        noEntryCards=0;
    }

    public Optional<Tower> getTower() {
        return tower;
    }

    public int getNumberOfTowers() {
        return numberOfTowers;
    }

    public EnumMap<Color, Integer> getStudents() {
        return students;
    }

    /**
     * @param color color of the students
     * @return the number of students of the color specified present on the island
     */
    public int getStudentsOf(Color color){
        return students.get(color);
    }

    public int getNoEntryCard() {
        return noEntryCards;
    }

    /**
     * @param color color of the students
     * @param number number of students of the specified color that have to be removed from the island
     */
    public void removeStudents(Color color, int number){
        if(number<=students.get(color))
            students.put(color, students.get(color)-number);
        else students.put(color, 0);
    }

    public void setTower(Tower tower) {
        this.tower=Optional.of(tower);
    }

    public void setNumberOfTowers(int numberOfTowers) {
        this.numberOfTowers = numberOfTowers;
    }

    public void setStudents(EnumMap<Color, Integer> students) {
        this.students = students;
        for(Color c : Color.values()){
            if(!students.containsKey(c))
                this.students.put(c,0);
        }
    }


    /**
     * @param color color of the students
     * @param number number of students of the specified color that have to be placed on the island
     */
    public void addStudents(Color color, int number){
        students.put(color, students.get(color)+number);
    }

    public void setNoEntryCard(int noEntryCard) {
        this.noEntryCards = noEntryCard;
    }


}
