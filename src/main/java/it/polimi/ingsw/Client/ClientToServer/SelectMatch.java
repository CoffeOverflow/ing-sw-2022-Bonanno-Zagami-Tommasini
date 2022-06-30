package it.polimi.ingsw.Client.ClientToServer;

/**
 * SelectMatch class
 * implementation of a setUp message from client to server to indicate the match to join
 * @author Federica Tommasini, Angelo Zagami
 */
public class SelectMatch implements SetUpMessage{
    private final int match;

    public SelectMatch(int match) {
        this.match = match;
    }

    public int getMatch() {
        return match;
    }
}
