package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Model.Wizards;
import it.polimi.ingsw.Server.ServerToClient.SelectWizard;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameController implements GUIController{
    private GUI gui;
    public AnchorPane gameBoard;
    public VBox selectWizard;
    public HBox listOfWizards;

    public void initialize() {
        gameBoard.setVisible(true);
        listOfWizards.setVisible(false);
        selectWizard.setVisible(false);
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message.replaceAll("\u001B\\[[\\d;]*[^\\d;]",""));
        //alert.setContentText("The entered IP/port doesn't match any active server or the server is not running. Please check errors and try again!");
        alert.showAndWait();
    }

    @Override
    public void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(message.replaceAll("\u001B\\[[\\d;]*[^\\d;]",""));
        alert.showAndWait();
    }

    public void selectWizard(SelectWizard wizards){

        listOfWizards.getChildren().clear();

        for(Wizards wizard : wizards.getAvailableWizards()){
            Image wizardImg = new Image(getClass().getResourceAsStream("/graphics/wizards/"+wizard.getFile()));
            ImageView wizardImgview = new ImageView(wizardImg);
            wizardImgview.setFitHeight(200);
            //wizardImgview.setFitWidth(494);
            wizardImgview.setPreserveRatio(true);
            //Creating a Button
            Button wizardButton = new Button();
            //Setting the size of the button
            wizardButton.setPrefSize(100, 200);
            //Setting a graphic to the button
            wizardButton.setGraphic(wizardImgview);
            listOfWizards.getChildren().add(wizardButton);
        }
        selectWizard.setVisible(true);
        listOfWizards.setVisible(true);


    }
}
