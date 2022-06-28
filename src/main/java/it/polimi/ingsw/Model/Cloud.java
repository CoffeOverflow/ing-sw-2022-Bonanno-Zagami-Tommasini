package it.polimi.ingsw.Model;

import java.util.EnumMap;

/**
 * Cloud class
 * @author Angelo Zagami
 */
public class Cloud {

    private EnumMap<Color, Integer> students = new EnumMap<Color, Integer>(Color.class);

    /**
     * Class constructor
     * Set student EnumMap<Color, Integer> at zero.
     */
    public Cloud(){
        for (Color color : Color.values()) {
            this.students.put(color, 0);
        }
    }

    /**
     * Set student on the cloud
     * @param students EnumMap<Color, Integer> that contains students to assign to cloud
     */
    public void setStudents(EnumMap<Color, Integer> students){
        this.students = students;
    }

    /**
     * Get students on the cloud
     * @return EnumMap<Color, Integer> Return students that are on cloud
     */
    public EnumMap<Color, Integer> getStudents(){
        return students; //Non so se dovrebbe ritornare una copia della variabile
    }
}
