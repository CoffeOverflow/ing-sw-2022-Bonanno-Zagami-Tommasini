package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Tower;

import java.util.HashMap;
import java.util.List;

public class MatchCreated implements ServerToClientMessage{



    private int motherNaturePosition;

    private HashMap<Integer, Color> mapStudentIsland; //at the beginning one student put on each island



    public MatchCreated(int motherNaturePosition, HashMap<Integer, Color> mapStudentIsland) {

        this.motherNaturePosition = motherNaturePosition;
        this.mapStudentIsland = mapStudentIsland;

    }

    public int getMotherNaturePosition() {
        return motherNaturePosition;
    }

    public HashMap<Integer, Color> getMapStudentIsland() {
        return mapStudentIsland;
    }
}
