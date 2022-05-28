package it.polimi.ingsw.Client.GUI;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


/**
 *  Scene controller class for the scene TestScene
 */
public class MainMenu
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
        button.setText("GIOCA");
    }


    public void buttonClicked(ActionEvent actionEvent)
    {
        //label.setText(textField.getText());
        button.setText("Siamo un po' indietro :c");
    }
}
