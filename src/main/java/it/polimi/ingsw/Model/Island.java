package it.polimi.ingsw.Model;

import java.util.EnumMap;
import java.util.Optional;

/** Island class
 * @author Federica Tommasini
 */
public class Island {

    Optional<Tower> tower;
    int numberOfTowers;
    EnumMap<Color,Integer> students;
    boolean noEntryCard;

    /**
     * class constructor
     */
    public Island(){
        students= new EnumMap<>(Color.class);
        numberOfTowers=0;
        noEntryCard=false;
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

    public int getStudentsOf(Color color){
        return students.get(color);
    }

    public boolean getNoEntryCard() {
        return noEntryCard;
    }

    public void removeStudents(Color color, int number){
        if(number<=students.get(color))
            students.put(color, students.get(color)-number);
        else students.put(color, 0);
    }

    public void setTower(Optional<Tower> tower) {
        this.tower = tower;
    }

    public void setNumberOfTowers(int numberOfTowers) {
        this.numberOfTowers = numberOfTowers;
    }

    public void setStudents(EnumMap<Color, Integer> students) {
        this.students = students;
    }

    public void addStudents(Color color, int number){
        students.put(color, students.get(color)+1);
    }

    public void setNoEntryCard(boolean noEntryCard) {
        this.noEntryCard = noEntryCard;
    }


}
