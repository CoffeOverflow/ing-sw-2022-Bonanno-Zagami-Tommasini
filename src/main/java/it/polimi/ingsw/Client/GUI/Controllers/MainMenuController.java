package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


/***
 * Controller for MainMenu phase
 * @author Angelo Zagami
 * @see GUIController
 */
public class MainMenuController implements GUIController
{
    private GUI gui;
    @FXML private AnchorPane mainPane;
    @FXML private Button button;
    @FXML private Button connect;
    @FXML private VBox connection;
    @FXML private TextField address;
    @FXML private TextField port;


    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        //alert.setContentText("The entered IP/port doesn't match any active server or the server is not running. Please check errors and try again!");
        alert.showAndWait();
        if(message.equals("Connection error, maybe one player left the match. The app will now close!"))
            System.exit(-1);
    }

    @Override
    public void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    /**
     * Method automatically called when the scene is loaded.
     * Initializes the scene.
     */
    public void initialize()
    {
        //label.setText("");
        button.setText("Play");
        connection.setVisible(false);
    }


    /***
     * Handler of play button
     * @param actionEvent Event that launch the method
     */
    public void playButtonClicked(ActionEvent actionEvent)
    {
        button.setVisible(false);
        connect.setText("CONNECT");
        connection.setVisible(true);
    }

    /***
     * Handler of connect button, it checks IP and Port parameter and connect to the server
     * @param actionEvent Event that launch the method
     */
    public void connectButtonClicked(ActionEvent actionEvent){
        if(!(address.getText().equals("")) && !(port.getText().equals(""))){
            int portNumber = 0;
            try{
                portNumber = Integer.parseInt(port.getText());
                if(portNumber > 1024){
                    Constants.setIP(address.getText());
                    Constants.setPort(Integer.parseInt(port.getText()));
                    try {
                        gui.setServerHandler(new ServerHandler());
                        gui.changeScene("SETUP");
                        Thread guiThread = new Thread(gui);
                        guiThread.start();
                    } catch (RuntimeException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Server unreachable or non-existent");
                        alert.setContentText("The entered IP/port doesn't match any active server or the server is not running. Please check errors and try again!");
                        alert.showAndWait();
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Port not valid");
                    alert.setContentText("Make sure that port number is greater than 1024!");
                    alert.showAndWait();
                }
            }catch (RuntimeException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Port not valid");
                alert.setContentText("Please insert a number!");
                alert.showAndWait();
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("IP address or port fields is empty");
            alert.setContentText("Verify that the fields are not empty and try again!");
            alert.showAndWait();
        }

    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
