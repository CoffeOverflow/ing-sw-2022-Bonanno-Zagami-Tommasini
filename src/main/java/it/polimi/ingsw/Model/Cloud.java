package it.polimi.ingsw.Model;

import java.util.EnumMap;

/**
 * Cloud class
 * @author Angelo Zagami
 */
public class Cloud {
    //I controlli per non scegliere due volte la stessa nuovola dovrebbero andare nel controller credo
    private EnumMap<Color, Integer> students = new EnumMap<Color, Integer>(Color.class);

    /**
     * Class constructor
     * Set student EnumMap<Color, Integer> at zero.
     */
    public Cloud(){
        for (Color color : Color.values()) {
            this.students.put(color, 0);
        }
        //this.students.forEach((k, v) -> this.students.put(k, 0));
    }

    /**
     * @param students EnumMap<Color, Integer> that contains students to assign to cloud
     */
    void setStudents(EnumMap<Color, Integer> students){
        this.students = students;
    }

    /**
     * @return EnumMap<Color, Integer> Return students that are on cloud
     */
    EnumMap<Color, Integer> getStudents(){
        return students; //Non so se dovrebbe ritornare una copia della variabile
    }
}
