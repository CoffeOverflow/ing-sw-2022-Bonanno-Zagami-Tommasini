package it.polimi.ingsw.Model;

public enum Wizards {
    OLDSAGE("Old sage", "oldsage.png"),
    KING("King", "king.png"),
    WITCH("Witch", "witch.png"),
    MONACO("Monaco", "monaco.png"),
    MOTHERNATURE("Mother nature", "mothernature.png");

    private String name;
    private String file;

    public String getName() {
        return name;
    }

    public String getFile(){
        return file;
    }

    private Wizards(String name, String file){
        this.name = name;
        this.file = file;
    }
}
