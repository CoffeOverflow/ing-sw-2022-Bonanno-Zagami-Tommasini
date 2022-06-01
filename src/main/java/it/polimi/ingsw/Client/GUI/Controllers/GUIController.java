package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;

public interface GUIController {
    public void setGUI(GUI gui);

    public void showError(String message);
}
