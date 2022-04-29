package it.polimi.ingsw.Client.ClientToServer;

public class SelectMatch implements SetUpMessage{
    private int match;

    public SelectMatch(int match) {
        this.match = match;
    }

    public int getMatch() {
        return match;
    }
}
