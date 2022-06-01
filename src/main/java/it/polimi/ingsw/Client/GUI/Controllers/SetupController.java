package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.ClientToServer.ChooseNickname;
import it.polimi.ingsw.Client.GUI.GUI;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SetupController implements GUIController{
    private GUI gui;
    public VBox requestNickname;
    public VBox chooseMatch;
    public TextField nickname;
    public Button send;
    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        //alert.setContentText("The entered IP/port doesn't match any active server or the server is not running. Please check errors and try again!");
        alert.showAndWait();
    }

    public void initialize() {
        requestNickname.setVisible(false);
        chooseMatch.setVisible(false);
    }

    public void showNicknameField(){
        requestNickname.setVisible(true);
    }
    public void showChooseMatch(){
        requestNickname.setVisible(false);
        chooseMatch.setVisible(true);
    }

    public void sendButtonClicked(){
        gui.send(new ChooseNickname(nickname.getText()));
    }
}
