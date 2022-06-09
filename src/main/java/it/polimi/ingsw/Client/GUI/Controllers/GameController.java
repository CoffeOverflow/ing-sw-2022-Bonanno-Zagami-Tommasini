package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.ClientToServer.ChooseCloud;
import it.polimi.ingsw.Client.ClientToServer.ChooseWizard;
import it.polimi.ingsw.Client.ClientToServer.PlayAssistantCard;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.GUI.GamePhase;
import it.polimi.ingsw.Client.VirtualModel;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Wizards;
import it.polimi.ingsw.Server.ServerToClient.SelectAssistantCard;
import it.polimi.ingsw.Server.ServerToClient.SelectWizard;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GameController implements GUIController{
    private GUI gui;
    private GamePhase currentPhase;
    @FXML public AnchorPane mainPane;
    public VBox selectWizard;
    public VBox waitForOtherWizard;
    public HBox listOfWizards;

    public AnchorPane boardAndOthersSchool;

    public ImageView thirdPlayerSchool;
    public ImageView cloud3;
    public ImageView mySchool;

    public AnchorPane cloudGrids;
    public HBox assistantCard;

    public List<GridPane> cloudGridsList=new ArrayList<>();



    public void initialize() {
        mainPane.setVisible(true);
        boardAndOthersSchool.setVisible(false);
        mySchool.setVisible(false);
        listOfWizards.setVisible(false);
        selectWizard.setVisible(false);
        waitForOtherWizard.setVisible(false);
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
                waitForOtherWizard.setVisible(true);
                //boardAndOthersSchool.setVisible(true);
                mySchool.setVisible(true);
                currentPhase = GamePhase.GAME;
                break;

            case ASSISTANT:
                assistantCard.setVisible(false);
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
        if(!message.contains("Match is starting")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(message.replaceAll("\u001B\\[[\\d;]*[^\\d;]",""));
            alert.showAndWait();
        }

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
        waitForOtherWizard.setVisible(false);
        boardAndOthersSchool.setVisible(true);
        if(gui.getVmodel().getPlayers().size()==2){
            thirdPlayerSchool.setVisible(false);
            cloud3.setVisible(false);
        }
        int count=0;
        for(Cloud cloud:gui.getVmodel().getClouds()){
            cloudGrids.getChildren().get(count).setUserData(count);
            cloudGrids.getChildren().get(count).addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                @Override public void handle(MouseEvent mouseEvent) {

                    if(currentPhase==GamePhase.FILLCLOUD){
                        chooseCloud(mouseEvent);
                    }else{
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("You can't take the students in this phase");
                    a.show();}
                }
            });
            cloudGridsList.add((GridPane)cloudGrids.getChildren().get(count));
            count++;
        }
    }
    public void showBoard(){
        this.showCloud();
        this.showSchool();

    }

    public void showSchool(){


    }

    public void showCloud(){
        VirtualModel vmodel=gui.getVmodel();
        int count=0;
        for(Cloud cloud: vmodel.getClouds()){
            int k=0;
            int j=0;
            for(Color color: cloud.getStudents().keySet()) {
                for (int i = 0; i < cloud.getStudents().get(color); i++) {
                    Image studentImg = new Image(getClass().getResourceAsStream("/graphics/board/" + color.getFileStudent()));
                    ImageView studentImgview = new ImageView(studentImg);
                    studentImgview.setFitHeight(15);
                    //wizardImgview.setFitWidth(494);
                    studentImgview.setPreserveRatio(true);
                    cloudGridsList.get(count).add(studentImgview, j, k);

                    if (j == 1) {
                        k++;
                        j = 0;
                    } else {
                        j++;
                    }
                }
            }
            count++;
        }
    }
    public void chooseWizard(Event event){
        Node node = (Node) event.getSource() ;
        String wizard = (String) node.getUserData();
        gui.send(new ChooseWizard(Wizards.valueOf(Wizards.class,wizard.replaceAll(" ", "").toUpperCase())));
    }

    public void selectAssistantCard(SelectAssistantCard msg){
        mySchool.setVisible(false);
        assistantCard.setVisible(true);
        currentPhase = GamePhase.ASSISTANT;
        assistantCard.getChildren().clear();
        int count = 1;
        for(String assistant : msg.getAvailableCards()){
            Image assistantImg = new Image(getClass().getResourceAsStream("/graphics/assistants/"+assistant.toLowerCase()+".png"));
            ImageView assistantImgview = new ImageView(assistantImg);
            assistantImgview.setFitHeight(150);
            //wizardImgview.setFitWidth(494);
            assistantImgview.setPreserveRatio(true);
            //Creating a Button
            Button assistantButton = new Button();
            //Setting the size of the button
            assistantButton.setPrefSize(60, 150);
            //Setting a graphic to the button
            assistantButton.setGraphic(assistantImgview);
            assistantButton.setUserData(assistant);
            assistantButton.setOnAction(new EventHandler() {
                @Override
                public void handle(Event event) {
                    chooseAssistant(event);
                }
            });
            assistantCard.getChildren().add(assistantButton);
        }
        assistantCard.setVisible(true);

    }

    public void chooseAssistant(Event event){
        Node node = (Node) event.getSource() ;
        String assistant = (String) node.getUserData();
        int value = 0;
        for(int i=0; i<gui.getVmodel().getClientPlayer().getAssistantCards().size();i++) {
            if (gui.getVmodel().getClientPlayer().getAssistantCards().get(i).getName().equalsIgnoreCase(assistant)) {
                        value = gui.getVmodel().getClientPlayer().getAssistantCards().get(i).getValue();
                        break;
            }

        }
        gui.send(new PlayAssistantCard(value));
        changePhase();
    }

    public void chooseCloud(MouseEvent event){
        Node node=(Node) event.getSource();
        int cloudId=(int) node.getUserData();
        gui.send((new ChooseCloud(cloudId)));
    }
}
