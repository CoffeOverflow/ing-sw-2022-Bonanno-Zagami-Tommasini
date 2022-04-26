package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Tower;

import java.util.HashMap;
import java.util.List;

public class MatchCreated implements ServerToClientMessage{

    private List<String> playerNicknames;

    private int numberOfPlayers;

    private boolean expertMode;

    private HashMap<String,String> mapPlayerWizard;

    private int motherNaturePosition;

    private HashMap<Integer, Color> mapStudentIsland; //at the beginning one student put on each island

    private HashMap<Integer, Tower> mapTowerToPlayer;

    //maps a player with the students placed in his entrance (each student mapped to the number)
    private HashMap<Integer,HashMap<Color,Integer>> mapPlayerEntranceStudent;

    private List<String> characterCards;

    public MatchCreated(List<String> playerNicknames, int numberOfPlayers, boolean expertMode, HashMap<String,
            String> mapPlayerWizard, int motherNaturePosition, HashMap<Integer, Color> mapStudentIsland, HashMap<Integer,
            Tower> mapTowerToPlayer, HashMap<Integer, HashMap<Color, Integer>> mapPlayerEntranceStudent) {
        this.playerNicknames = playerNicknames;
        this.numberOfPlayers = numberOfPlayers;
        this.expertMode = expertMode;
        this.mapPlayerWizard = mapPlayerWizard;
        this.motherNaturePosition = motherNaturePosition;
        this.mapStudentIsland = mapStudentIsland;
        this.mapTowerToPlayer = mapTowerToPlayer;
        this.mapPlayerEntranceStudent = mapPlayerEntranceStudent;

    }

    public MatchCreated(List<String> playerNicknames, int numberOfPlayers, boolean expertMode, HashMap<String,
            String> mapPlayerWizard, int motherNaturePosition, HashMap<Integer, Color> mapStudentIsland, HashMap<Integer,
            Tower> mapTowerToPlayer, HashMap<Integer, HashMap<Color, Integer>> mapPlayerEntranceStudent, List<String> characterCards) {
        this.playerNicknames = playerNicknames;
        this.numberOfPlayers = numberOfPlayers;
        this.expertMode = expertMode;
        this.mapPlayerWizard = mapPlayerWizard;
        this.motherNaturePosition = motherNaturePosition;
        this.mapStudentIsland = mapStudentIsland;
        this.mapTowerToPlayer = mapTowerToPlayer;
        this.mapPlayerEntranceStudent = mapPlayerEntranceStudent;
        this.characterCards=characterCards;

    }
}
