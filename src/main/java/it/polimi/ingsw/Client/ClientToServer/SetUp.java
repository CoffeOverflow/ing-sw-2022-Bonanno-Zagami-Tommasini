package it.polimi.ingsw.Client.ClientToServer;

public class SetUp implements ClientToServerMessage {

    private int numberOfPlayers;

    private boolean expertMode;

    public SetUp(int numberOfPlayers, boolean mode){
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
