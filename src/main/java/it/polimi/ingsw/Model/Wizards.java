package it.polimi.ingsw.Model;

public enum Wizards {
    OLDSAGE("Old sage", "oldsage.png","oldsageCut.png"),
    KING("King", "king.png","kingCut.png"),
    WITCH("Witch", "witch.png","witchCut.png"),
    MONACO("Monaco", "monaco.png","monacoCut.png"),
    MOTHERNATURE("Mother nature", "mothernature.png","mothernatureCut.png");

    private String name;
    private String file;

    public String getCutFile() {return cutFile; }

    private String cutFile;

    public String getName() {
        return name;
    }

    public String getFile(){
        return file;
    }

    private Wizards(String name, String file,String cutFile){
        this.name = name;
        this.file = file;
        this.cutFile=cutFile;
    }
}
