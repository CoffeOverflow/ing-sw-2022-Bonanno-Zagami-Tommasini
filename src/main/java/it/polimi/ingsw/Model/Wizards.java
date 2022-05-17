package it.polimi.ingsw.Model;

public enum Wizards {
    OLDSAGE("Old sage"),
    KING("King"),
    WITCH("Witch"),
    MONACO("Monaco"),
    MOTHERNATURE("Mother nature");

    private String name;

    public String getName() {
        return name;
    }

    private Wizards(String name){
        this.name = name;
    }
}
