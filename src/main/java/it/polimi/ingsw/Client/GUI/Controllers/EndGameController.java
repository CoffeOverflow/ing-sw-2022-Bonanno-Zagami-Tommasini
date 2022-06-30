package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/***
 * Controller for EndGame phase
 * @author Angelo Zagami
 * @see GUIController
 */
public class EndGameController implements GUIController{
    private GUI gui;

    @FXML private AnchorPane mainPane;
    @FXML private ImageView endImg;
    @FXML private Text lose;

    /***
     * Initialization method
     */
    public void initialize() {
        mainPane.setVisible(true);
        lose.setVisible(false);
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    /***
     * Show the "you win" phase
     */
    public void win(){
        endImg.setImage(new Image(getClass().getResourceAsStream("/graphics/additionalElement/win.png")));
        endImg.setFitHeight(360);
        endImg.setPreserveRatio(true);
        endImg.setVisible(true);
    }

    /***
     * Show the "lose" message
     * @param message Lose message
     */
    public void lose(String message){
        lose.setText(message);
        lose.setVisible(true);
        endImg.setImage(new Image(getClass().getResourceAsStream("/graphics/additionalElement/lose.png")));
        endImg.setFitHeight(400);
        endImg.setPreserveRatio(true);
        endImg.setVisible(true);
    }

    @Override
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message.replaceAll("\u001B\\[[\\d;]*[^\\d;]",""));
        //alert.setContentText("The entered IP/port doesn't match any active server or the server is not running. Please check errors and try again!");
        alert.showAndWait();
        if(message.equals("Connection error, maybe one player left the match. The app will now close!"))
            System.exit(-1);
    }

    @Override
    public void showMessage(String message) {
        if(!message.contains("Match is starting")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(message.replaceAll("\u001B\\[[\\d;]*[^\\d;]",""));
            alert.showAndWait();
        }

    }
}
