package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.ClientToServer.ChooseWizard;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.GUI.GamePhase;
import it.polimi.ingsw.Client.VirtualModel;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Wizards;
import it.polimi.ingsw.Server.ServerToClient.SelectWizard;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class GameController implements GUIController{
    private GUI gui;
    private GamePhase currentPhase;
    @FXML public AnchorPane mainPane;
    public VBox selectWizard;
    public HBox listOfWizards;

    public AnchorPane boardAndOthersSchool;

    public ImageView thirdPlayerSchool;
    public ImageView cloud3;
    public ImageView mySchool;

    public AnchorPane cloudGrids;

    public List<GridPane> cloudGridsList=new ArrayList<>();



    public void initialize() {
        mainPane.setVisible(true);
        boardAndOthersSchool.setVisible(false);
        mySchool.setVisible(false);
        listOfWizards.setVisible(false);
        selectWizard.setVisible(false);
        currentPhase = GamePhase.WIZARD;


    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void changePhase(){
        switch (currentPhase){
            case WIZARD:
                selectWizard.setVisible(false);
                boardAndOthersSchool.setVisible(true);
                mySchool.setVisible(true);
                currentPhase = GamePhase.GAME;
                break;
        }
    }

    public void changePhase(GamePhase phase){
        currentPhase = phase;
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
            wizardButton.setUserData(wizard.getName());
            wizardButton.setOnAction(new EventHandler() {
                @Override
                public void handle(Event event) {
                    chooseWizard(event);
                }
            });
            listOfWizards.getChildren().add(wizardButton);
        }
        selectWizard.setVisible(true);
        listOfWizards.setVisible(true);


    }

    public void setGrids(){
        if(gui.getVmodel().getPlayers().size()==2){
            thirdPlayerSchool.setVisible(false);
            cloud3.setVisible(false);
        }
        int count=0;
        for(Cloud cloud:gui.getVmodel().getClouds()){
            cloudGridsList.add((GridPane)cloudGrids.getChildren().get(count));
            count++;
        }
    }
    public void showBoard(){
        setGrids();
        VirtualModel vmodel=gui.getVmodel();
        int count=0;
        for(Cloud cloud: vmodel.getClouds()){
            for(Color color: cloud.getStudents().keySet()) {
                int k=0;
                int j=0;
                for (int i = 0; i < cloud.getStudents().get(color); i++) {
                    Button studentButton = new Button();
                    Image studentImg = new Image(getClass().getResourceAsStream("/graphics/board/" + color.getFileStudent()));
                    ImageView studentImgview = new ImageView(studentImg);
                    studentImgview.setFitHeight(20);
                    //wizardImgview.setFitWidth(494);
                    studentImgview.setPreserveRatio(true);
                    studentButton.setPrefSize(5, 5);
                    studentButton.setGraphic(studentImgview);
                    cloudGridsList.get(count).add(studentButton, k, j);
                    if(j==1){
                        k++;
                        j=0;
                    }else{
                        j++;
                    }

                }

            }
        }
    }

    public void chooseWizard(Event event){
        Node node = (Node) event.getSource() ;
        String wizard = (String) node.getUserData();
        gui.send(new ChooseWizard(Wizards.valueOf(Wizards.class,wizard.replaceAll(" ", "").toUpperCase())));
    }
}
