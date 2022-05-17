package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Tower;

import java.io.Serializable;
import java.util.EnumMap;

public class BoardChange implements Serializable{
    private MoveTo moveTo;

    private Color studentColor;

    private int islandPosition;

    private int conquerIsland;

    private int motherNatureSteps;

    private Tower conquerorTower;

    private int mergedIsland1;

    private int mergedIsland2;


    private int playerID;

    private EnumMap<Color, Integer> students1 = new EnumMap<Color, Integer>(Color.class);
    private EnumMap<Color, Integer> students2= new EnumMap<Color, Integer>(Color.class);
    private EnumMap<Color, Integer> students3 = new EnumMap<Color, Integer>(Color.class); //null if only two players

    private boolean isConquered=false;

    private Change change;

    public BoardChange(MoveTo moveTo, Color studentColor, int islandPosition,int playerID) {
        this.moveTo = moveTo;
        this.studentColor = studentColor;
        this.islandPosition = islandPosition;
        this.playerID=playerID;
        this.change=Change.MOVESTUDENT;
    }

    public BoardChange(int motherNatureSteps) {
        this.motherNatureSteps = motherNatureSteps;
        this.change=Change.MOTHERNATURE;
    }

    public BoardChange(Tower conquerorTower,int conquerIsland) {
        this.isConquered = true;
        this.conquerorTower = conquerorTower;
        this.conquerIsland=conquerIsland;
        this.change=Change.CONQUER;
    }

    public BoardChange(int mergedIsland1, int mergedIsland2) {
        this.mergedIsland1 = mergedIsland1;
        this.mergedIsland2 = mergedIsland2;
        this.isConquered = true;
        this.change=Change.MERGE;
    }

    public BoardChange(EnumMap<Color, Integer> students1,EnumMap<Color, Integer> students2,EnumMap<Color, Integer> students3){
        this.students1=students1;
        this.students2=students2;
        this.students3=students3;
        this.change=Change.CLOUD;
    }

    public BoardChange(EnumMap<Color, Integer> students1,EnumMap<Color, Integer> students2){
        this.students1=students1;
        this.students2=students2;
        this.change=Change.CLOUD;
    }


    public MoveTo getMoveTo() {
        return moveTo;
    }

    public Color getStudentColor() {
        return studentColor;
    }

    public int getIslandPosition() {
        return islandPosition;
    }

    public int getMotherNatureSteps() {
        return motherNatureSteps;
    }

    public Tower getConquerorTower() {
        return conquerorTower;
    }

    public int getMergedIsland1() {
        return mergedIsland1;
    }

    public int getMergedIsland2() {
        return mergedIsland2;
    }

    public boolean isConquered() {
        return isConquered;
    }

    public Change getChange() { return change; }

    public int getPlayer() {return playerID;}

    public int getConquerIsland() {return conquerIsland;}

    public EnumMap<Color, Integer> getStudents1() {
        return students1;
    }

    public EnumMap<Color, Integer> getStudents2() {
        return students2;
    }

    public EnumMap<Color, Integer> getStudents3() {
        return students3;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
