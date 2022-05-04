package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Tower;

import java.io.Serializable;

public class BoardChange implements Serializable{
    private MoveTo moveTo;

    private Color studentColor;

    private int islandPosition;

    private int motherNatureSteps;

    private Tower conquerorTower;

    private int mergedIsland1;

    private int mergedIsland2;

    private boolean isConquered=false;

    public BoardChange(MoveTo moveTo, Color studentColor, int islandPosition) {
        this.moveTo = moveTo;
        this.studentColor = studentColor;
        this.islandPosition = islandPosition;
    }

    public BoardChange(int motherNatureSteps) {
        this.motherNatureSteps = motherNatureSteps;
    }

    public BoardChange(Tower conquerorTower) {
        this.isConquered = true;
        this.conquerorTower = conquerorTower;
    }

    public BoardChange(int mergedIsland1, int mergedIsland2) {
        this.mergedIsland1 = mergedIsland1;
        this.mergedIsland2 = mergedIsland2;
        this.isConquered = true;
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

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
