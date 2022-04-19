package it.polimi.ingsw.Model;

/**
 * Assistant card class
 * @author Angelo Zagami
 */
public class AssistantCard {

    private final int value;
    private final int movement;
    private final String asset;

    /**
     * Class constructor
     * @param value Value of card
     * @param movement Number of possible steps of Mother nature
     * @param asset Path of graphical asset
     */
    public AssistantCard(int value, int movement, String asset){
        this.value = value;
        this.movement = movement;
        this.asset = asset;
    }

    public int getValue() {
        return value;
    }

    public int getMovement() {
        return movement;
    }

    public String getAsset() {
        return asset;
    }
}
