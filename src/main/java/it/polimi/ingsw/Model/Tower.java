package it.polimi.ingsw.Model;

public enum Tower {
    WHITE("white_tower.png"),
    BLACK("black_tower.png"),
    GRAY("grey_tower.png");

    private String file;
    public String getFile() {

        return file;
    }

    private Tower(String file){
        this.file=file;
    }
}
