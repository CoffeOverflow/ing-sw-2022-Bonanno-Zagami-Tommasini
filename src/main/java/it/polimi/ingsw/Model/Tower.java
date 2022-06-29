package it.polimi.ingsw.Model;

/**
 * @author Giuseppe Bonanno
 * The enum of the towers with the relative image that will be used for the GUI
 */
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
