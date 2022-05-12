package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Model.Color;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;

public class SetUpSchoolStudent implements ServerToClientMessage{

    private EnumMap<Color, Integer> entranceStudents;

    private Integer playerID;

    public SetUpSchoolStudent(EnumMap<Color, Integer> entranceStudents, Integer playerID) {
        this.entranceStudents = entranceStudents;
        this.playerID = playerID;
    }

    public EnumMap<Color, Integer> getEntranceStudents() {
        return entranceStudents;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    @Override
    public void handle(View view) throws IOException {

    }
}
