package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Tower;

import java.io.Serializable;

public class BoardChange implements Serializable{
    private MoveTo moveTo;

    private Color studentColor;

    private int islandPosition;

    private int conquerIsland;

    private int motherNatureSteps;

    private Tower conquerorTower;

    private int mergedIsland1;

    private int mergedIsland2;


    private Player player;

    private boolean isConquered=false;

    private Change change;

    public BoardChange(MoveTo moveTo, Color studentColor, int islandPosition,Player player) {
        this.moveTo = moveTo;
        this.studentColor = studentColor;
        this.islandPosition = islandPosition;
        this.player=player;
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

    public Player getPlayer() {return player;}

    public int getConquerIsland() {return conquerIsland;}

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
