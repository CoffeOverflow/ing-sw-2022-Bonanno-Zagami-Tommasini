package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Model.Tower;
import it.polimi.ingsw.Model.Wizards;
import java.io.IOException;
import java.util.HashMap;

/**
 * implementation of a message from server to client to inform the client of other players' info (nicknames, towers, wizards)
 * @author Federica Tommasini
 */
public class PlayersInfo implements ServerToClientMessage{

    private HashMap<Integer,String> mapIDNickname;

    private int numberOfTowers;

    private boolean expertMode;

    private HashMap<Integer, Wizards> mapPlayerWizard;

    private HashMap<Integer, Tower> mapTowerToPlayer;

    private int yourPlayerID;

    public PlayersInfo(boolean expertMode, int numberOfTowers, HashMap<Integer, Wizards> mapPlayerWizard,
                       HashMap<Integer, Tower> mapTowerToPlayer,HashMap<Integer,String> mapIDNickname, int yourPlayerID) {
        this.numberOfTowers=numberOfTowers;
        this.expertMode = expertMode;
        this.mapPlayerWizard = mapPlayerWizard;
        this.mapTowerToPlayer=mapTowerToPlayer;
        this.mapIDNickname=mapIDNickname;
        this.yourPlayerID=yourPlayerID;
    }


    public int getNumberOfTowers() {
        return numberOfTowers;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    public HashMap<Integer, Wizards> getMapPlayerWizard() {
        return mapPlayerWizard;
    }

    public HashMap<Integer, Tower> getMapTowerToPlayer() {
        return mapTowerToPlayer;
    }

    public HashMap<Integer, String> getMapIDNickname() {
        return mapIDNickname;
    }

    public int getYourPlayerID() {
        return yourPlayerID;
    }

    @Override
    public void handle(View view) throws IOException {
        view.playersInfo(this);
    }
}
