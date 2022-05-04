package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Model.Color;

import java.io.IOException;
import java.util.HashMap;

public class SetUpCharacterCard implements ServerToClientMessage{

    private String[] characterCards;

    private HashMap<Color, Integer> firstCardStudents;

    private HashMap<Color, Integer> secondCardStudents;

    private HashMap<Color, Integer> thirdCardStudents;

    public SetUpCharacterCard(String[] characterCards) {
        this.characterCards = characterCards;
    }

    public void setFirstCardStudents(HashMap<Color, Integer> firstCardStudents) {
        this.firstCardStudents = firstCardStudents;
    }

    public void setSecondCardStudents(HashMap<Color, Integer> secondCardStudents) {
        this.secondCardStudents = secondCardStudents;
    }

    public void setThirdCardStudents(HashMap<Color, Integer> thirdCardStudents) {
        this.thirdCardStudents = thirdCardStudents;
    }

    public String[] getCharacterCards() {
        return characterCards;
    }

    public HashMap<Color, Integer> getFirstCardStudents() {
        return firstCardStudents;
    }

    public HashMap<Color, Integer> getSecondCardStudents() {
        return secondCardStudents;
    }

    public HashMap<Color, Integer> getThirdCardStudents() {
        return thirdCardStudents;
    }

    @Override
    public void handle(View view) throws IOException {

    }
}
