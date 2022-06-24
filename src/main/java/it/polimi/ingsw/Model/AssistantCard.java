package it.polimi.ingsw.Model;

import java.io.Serializable;

/**
 * Assistant card class
 * @author Angelo Zagami
 */
public class AssistantCard implements Serializable {

    private final int value;
    private final int mothernaturesteps;
    private final String name;
    private final String asset;

    /**
     * Class constructor
     * @param value Value of card
     * @param mothernaturesteps Number of possible steps of Mother nature
     * @param asset Path of graphical asset
     */
    public AssistantCard(int value, int mothernaturesteps, String name, String asset){
        this.value = value;
        this.mothernaturesteps = mothernaturesteps;
        this.name = name;
        this.asset = asset;
    }

    public int getValue() {
        return value;
    }

    public int getMothernatureSteps() {
        return mothernaturesteps;
    }
    public String getName() {
        return name;
    }
    public String getAsset() {
        return asset;
    }


    @Override
    public String toString() {
        return "AssistantCard{" +
                "value=" + value +
                ", mothernatureSteps=" + mothernaturesteps +
                ", name='" + name + '\'' +
                ", asset='" + asset + '\'' +
                '}' + '\n';
    }
}
