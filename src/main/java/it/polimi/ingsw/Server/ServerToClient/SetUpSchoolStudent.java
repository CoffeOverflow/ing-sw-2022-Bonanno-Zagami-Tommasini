package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Model.Color;

import java.io.IOException;
import java.util.EnumMap;

/**
 * implementation of a message from server to client to set up the entrance students of a player
 * @author Federica Tommasini, Giuseppe Bonanno
 */
public class SetUpSchoolStudent implements ServerToClientMessage{

    private final EnumMap<Color, Integer> entranceStudents;

    private final Integer playerID;

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
        view.setUpSchoolStudent(this);
    }
}
