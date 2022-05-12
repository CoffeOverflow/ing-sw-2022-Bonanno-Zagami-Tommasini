package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Model.Tower;

import java.io.IOException;
import java.util.HashMap;

public class PlayersInfo implements ServerToClientMessage{

    private HashMap<Integer,String> mapIDNickname;

    private int numberOfTowers;

    private boolean expertMode;

    private HashMap<Integer,String> mapPlayerWizard;

    private HashMap<Integer, Tower> mapTowerToPlayer;


    public PlayersInfo( int numberOfPlayers, boolean expertMode, HashMap<Integer, String> mapPlayerWizard,
                       HashMap<Integer, Tower> mapTowerToPlayer,HashMap<Integer,String> mapIDNickname) {

        this.expertMode = expertMode;
        this.mapPlayerWizard = mapPlayerWizard;
        this.mapTowerToPlayer=mapTowerToPlayer;
        this.mapIDNickname=mapIDNickname;
    }


    public int getNumberOfTowers() {
        return numberOfTowers;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    public HashMap<Integer, String> getMapPlayerWizard() {
        return mapPlayerWizard;
    }

    public HashMap<Integer, Tower> getMapTowerToPlayer() {
        return mapTowerToPlayer;
    }

    public HashMap<Integer, String> getMapIDNickname() {
        return mapIDNickname;
    }

    @Override
    public void handle(View view) throws IOException {

    }
}
