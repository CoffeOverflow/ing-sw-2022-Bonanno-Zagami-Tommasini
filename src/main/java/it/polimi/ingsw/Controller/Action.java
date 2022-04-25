package it.polimi.ingsw.Controller;


import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Tower;

import java.util.EnumMap;
import java.util.Map;

public class Action {
    private int motherNatureSteps;
    private int posIsland;
    private Color colorStudent;
    private MoveTo move;
    private int chooseCloud;


    public int getChooseCloud() {
        return chooseCloud;
    }

    public void setChooseCloud(int chooseCloud) {
        this.chooseCloud = chooseCloud;
    }

    public MoveTo getMove() {
        return move;
    }

    public void setMove(MoveTo move) {
        this.move = move;
    }


    public int getPosIsland() {
        return posIsland;
    }

    public void setPosIsland(int posIsland) {
        this.posIsland = posIsland;
    }

    public Color getColorStudent() {
        return colorStudent;
    }

    public void setColorStudent(Color color) {
        this.colorStudent = color;
    }


    public int getMotherNatureSteps() {
        return motherNatureSteps;
    }

    public void setMotherNatureSteps(int motherNatureSteps) {
        this.motherNatureSteps = motherNatureSteps;
    }




}
