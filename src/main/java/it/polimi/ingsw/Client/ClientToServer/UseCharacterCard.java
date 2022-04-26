package it.polimi.ingsw.Client.ClientToServer;

public class UseCharacterCard implements ClientToServerMessage{

    private String asset;
    //TODO capire che attributi aggiungere in base alla carta

    public UseCharacterCard(String asset){
        this.asset=asset;
    }

    public String getAsset() {
        return asset;
    }
}
