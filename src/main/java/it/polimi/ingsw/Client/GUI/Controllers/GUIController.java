package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;

/***
 * GUI Controller Interface
 * @author Angelo Zagami
 */
public interface GUIController {
    /***
     * Set GUI
     * @param gui The GUI object
     */
    public void setGUI(GUI gui);

    /***
     * Shows a popup with error message
     * @param message The message to show
     */
    public void showError(String message);

    /***
     * Shows a popup with message
     * @param message The message to show
     */
    public void showMessage(String message);
}
