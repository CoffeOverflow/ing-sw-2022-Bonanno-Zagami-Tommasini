package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Model.Color;

import java.io.IOException;
import java.util.HashMap;

public class SetUpSchoolStudent implements ServerToClientMessage{

    private HashMap<Color, Integer> entranceStudents;

    private String playerNickname;

    public SetUpSchoolStudent(HashMap<Color, Integer> entranceStudents, String playerNickname) {
        this.entranceStudents = entranceStudents;
        this.playerNickname = playerNickname;
    }

    public HashMap<Color, Integer> getEntranceStudents() {
        return entranceStudents;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    @Override
    public void handle(View view) throws IOException {

    }
}
