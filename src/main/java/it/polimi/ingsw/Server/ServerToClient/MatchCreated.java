package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Model.Color;
import java.io.IOException;
import java.util.HashMap;

/**
 * implementation of a message from server to client to inform the client of the initial configuration of the islands and mother nature
 * @author Federica Tommasini
 */
public class MatchCreated implements ServerToClientMessage{

    private final int motherNaturePosition;

    /*
     * at the beginning one student for each island is set
     */
    private HashMap<Integer, Color> mapStudentIsland;

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

    @Override
    public void handle(View view) throws IOException {
        view.matchCreated(this);
    }
}
