package it.polimi.ingsw.Model;

import java.io.Serializable;

/**
 * Assistant card class
 * @author Angelo Zagami
 */
public class AssistantCard implements Serializable {

    private final int value;
    private final int mothernatureSteps;
    private final String name;
    private final String asset;

    /**
     * Class constructor
     * @param value Value of card
     * @param mothernatureSteps Number of possible steps of Mother nature
     * @param asset Path of graphical asset
     */
    public AssistantCard(int value, int mothernatureSteps, String name, String asset){
        this.value = value;
        this.mothernatureSteps = mothernatureSteps;
        this.name = name;
        this.asset = asset;
    }

    /***
     * Get the card value
     * @return The card value
     */
    public int getValue() {
        return value;
    }

    /***
     * Get mothernature steps
     * @return Mothernature steps
     */
    public int getMothernatureSteps() {
        return mothernatureSteps;
    }

    /***
     * Get name of card
     * @return The name of card
     */
    public String getName() {
        return name;
    }

    /**+
     * Get the graphical asset used in GUI version of the game
     * @return The asset of the card
     */
    public String getAsset() {
        return asset;
    }


    /***
     * Get a string with card information
     * @return String version of the class
     */
    @Override
    public String toString() {
        return "AssistantCard{" +
                "value=" + value +
                ", mothernatureSteps=" + mothernatureSteps +
                ", name='" + name + '\'' +
                ", asset='" + asset + '\'' +
                '}' + '\n';
    }
}
