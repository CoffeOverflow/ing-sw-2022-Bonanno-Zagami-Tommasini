package it.polimi.ingsw.Client.ClientToServer;

/**
 * SelectModeAndPlayers class
 * implementation of a setUp message from client to server to indicate the number of players and the match's mode
 * chosen by the player who created the match
 * @author Federica Tommasini
 */
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
