package it.polimi.ingsw.Client.ClientToServer;

public class SelectMatch implements SetUpMessage{
    private String match;

    public SelectMatch(String match) {
        this.match = match;
    }

    public String getMatch() {
        return match;
    }
}
