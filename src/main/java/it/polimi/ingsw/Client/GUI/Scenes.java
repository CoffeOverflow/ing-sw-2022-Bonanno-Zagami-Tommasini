package it.polimi.ingsw.Client.GUI;

public enum Scenes {

    MENU("MENU", "MainMenu.fxml"),
    SETUP("SETUP", "Setup.fxml"),
    GAME("GAME", "Game.fxml");
    private final String name;
    private final String file;
    private Scenes(String name, String file) {
        this.file = file;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getFile() {
        return file;
    }
}
