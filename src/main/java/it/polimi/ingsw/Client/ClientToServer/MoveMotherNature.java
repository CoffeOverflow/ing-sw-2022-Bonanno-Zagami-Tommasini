package it.polimi.ingsw.Client.ClientToServer;

public class MoveMotherNature implements ClientToServerMessage{
    private int steps;

    public MoveMotherNature(int steps){
        this.steps=steps;
    }

    public int getSteps() {
        return steps;
    }
}
