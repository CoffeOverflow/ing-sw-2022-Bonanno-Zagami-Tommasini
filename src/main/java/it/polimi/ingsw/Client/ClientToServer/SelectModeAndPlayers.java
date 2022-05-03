package it.polimi.ingsw.Client.ClientToServer;

public class SelectModeAndPlayers implements SetUpMessage {

    private int numberOfPlayers;

    private boolean expertMode;

    public SelectModeAndPlayers(int numberOfPlayers, boolean mode){
        this.numberOfPlayers=numberOfPlayers;
        this.expertMode=mode;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public boolean isExpertMode() {
        return expertMode;
    }
}
