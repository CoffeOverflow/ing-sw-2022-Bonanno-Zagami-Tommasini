package it.polimi.ingsw.Client.ClientToServer;

public class ChooseCloud implements ClientToServerMessage{

    private int cloud;

    public ChooseCloud(int cloud){
        this.cloud=cloud;
    }

    public int getCloud() {
        return cloud;
    }
}
