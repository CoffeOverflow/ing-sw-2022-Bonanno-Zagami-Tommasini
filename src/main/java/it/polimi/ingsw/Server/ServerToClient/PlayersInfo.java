package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Model.Tower;

import java.util.HashMap;
import java.util.List;

public class PlayersInfo implements ServerToClientMessage{
    private List<String> playerNicknames;

    private int numberOfPlayers;

    private boolean expertMode;

    private HashMap<String,String> mapPlayerWizard;

    private HashMap<String, Tower> mapTowerToPlayer;


    public PlayersInfo(List<String> playerNicknames, int numberOfPlayers, boolean expertMode, HashMap<String, String> mapPlayerWizard,
                       HashMap<String, Tower> mapTowerToPlayer) {
        this.playerNicknames = playerNicknames;
        this.numberOfPlayers = numberOfPlayers;
        this.expertMode = expertMode;
        this.mapPlayerWizard = mapPlayerWizard;
        this.mapTowerToPlayer=mapTowerToPlayer;
    }

    public List<String> getPlayerNicknames() {
        return playerNicknames;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    public HashMap<String, String> getMapPlayerWizard() {
        return mapPlayerWizard;
    }

    public HashMap<String, Tower> getMapTowerToPlayer() {
        return mapTowerToPlayer;
    }
}
