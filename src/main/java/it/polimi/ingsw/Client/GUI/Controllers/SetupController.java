package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;

public class SetupController implements GUIController{
    private GUI gui;
    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
