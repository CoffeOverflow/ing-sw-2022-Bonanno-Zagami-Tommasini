package it.polimi.ingsw.Client.GUI;

/***
 * Enum class for FXML scenes for the GUI implementation
 * @author Angelo Zagami
 */
public enum Scenes {

    MENU("MENU", "MainMenu.fxml"),
    SETUP("SETUP", "Setup.fxml"),
    GAME("GAME", "Game.fxml"),
    ENDGAME("ENDGAME", "EndGame.fxml");
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
