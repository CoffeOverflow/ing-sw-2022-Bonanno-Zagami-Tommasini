package it.polimi.ingsw.Client.GUI.Controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;


/**
 *  Scene controller class for the scene TestScene
 */
public class MainMenuController
{
    public Button button;
    /*public Label label;
    public TextField textField;*/


    /**
     * Method automatically called when the scene is loaded.
     * Initializes the scene.
     */
    public void initialize()
    {
        //label.setText("");
        button.setText("Play");
    }


    public void buttonClicked(ActionEvent actionEvent)
    {
        //label.setText(textField.getText());
        button.setText("Siamo un po' indietro :c");
    }
}
