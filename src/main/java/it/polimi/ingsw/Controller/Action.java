package it.polimi.ingsw.Controller;


import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Tower;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Action {
    private int motherNatureSteps;
    private Integer posIsland;
    private Color colorStudent;
    private MoveTo move;
    private int chooseCloud;

    /**
     * attributes for the use of character cards
     */
    private String asset;
    private EnumMap<Color, Integer> chosenStudents;
    private EnumMap<Color, Integer> entranceStudents;
    private int chosenNumberOfSteps;
    private Color chosenColor;


    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

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

    public EnumMap<Color, Integer> getChosenStudents() {
        return chosenStudents;
    }

    public void setChosenStudents(EnumMap<Color, Integer> chosenStudents) {
        this.chosenStudents = chosenStudents;
    }

    public EnumMap<Color, Integer> getEntranceStudents() {
        return entranceStudents;
    }

    public void setEntranceStudents(EnumMap<Color, Integer> entranceStudents) {
        this.entranceStudents = entranceStudents;
    }

    public int getChosenNumberOfSteps() {
        return chosenNumberOfSteps;
    }

    public void setChosenNumberOfSteps(int chosenNumberOfSteps) {
        this.chosenNumberOfSteps = chosenNumberOfSteps;
    }

    public Color getChosenColor() {
        return chosenColor;
    }

    public void setChosenColor(Color chosenColor) {
        this.chosenColor = chosenColor;
    }
}
