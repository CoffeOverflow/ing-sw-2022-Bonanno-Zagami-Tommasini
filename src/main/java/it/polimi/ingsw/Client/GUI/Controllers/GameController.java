package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.ClientToServer.ChooseCloud;
import it.polimi.ingsw.Client.ClientToServer.ChooseWizard;
import it.polimi.ingsw.Client.ClientToServer.PlayAssistantCard;
import it.polimi.ingsw.Client.ClientToServer.UseCharacterCard;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.GUI.GamePhase;
import it.polimi.ingsw.Client.VirtualModel;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Server.ServerToClient.ChooseOption;
import it.polimi.ingsw.Server.ServerToClient.SelectAssistantCard;
import it.polimi.ingsw.Server.ServerToClient.SelectWizard;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.EnumMap;
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
    public HBox character;
    public HBox charAndMoney;
    public AnchorPane secondSchoolPane;
    public AnchorPane thirdSchoolPane;
    public AnchorPane mySchoolPane;
    public GridPane moneyWizardGrid;
    public GridPane wizardGrid;

    public List<GridPane> cloudGridsList=new ArrayList<>();
    public List<GridPane> schoolEntranceGridsList=new ArrayList<>();
    public List<GridPane> schoolDiningGridsList=new ArrayList<>();
    public List<GridPane> schoolTowersGridsList=new ArrayList<>();
    private Integer[] selectedStudent = null;

    public List<GridPane> islandGridsList=new ArrayList<>();
    public List<Group> islandGroupsList=new ArrayList<>();
    public GridPane cardGrid1;
    public GridPane cardGrid2;
    public GridPane cardGrid3;

    private Integer posIsland=null;
    private EnumMap<Color,Integer> choosenStudent=null;
    private EnumMap<Color,Integer> entranceStudent=null;
    private Color color=null;


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
                character.setVisible(true);
                wizardGrid.setVisible(true);
                moneyWizardGrid.setVisible(true);
                cardGrid1.setVisible(true);
                cardGrid2.setVisible(true);
                cardGrid3.setVisible(true);
                for(int i=0;i<3;i++){
                    character.getChildren().get(i).setDisable(false);
                }
                currentPhase = GamePhase.GAME;
                this.showBoard();
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

    public void showCharacterCard(){
        character.getChildren().clear();
        List<CharacterCard> characterCards=gui.getVmodel().getCharacterCards();
        List<GridPane> cardGridList=new ArrayList<>();
        cardGridList.add(cardGrid1);
        cardGridList.add(cardGrid2);
        cardGridList.add(cardGrid3);
        int countForCardGrid=0;
        for(CharacterCard charCard:characterCards){
            int j=0;
            int k=0;
            Image characterImg = new Image(getClass().getResourceAsStream("/graphics/character/"+charCard.getAsset()));
            ImageView characterImgview = new ImageView(characterImg);
            characterImgview.setFitHeight(150);
            characterImgview.setPreserveRatio(true);
            //Creating a Button
            Button characterButton = new Button();
            //Setting the size of the button
            characterButton.setPrefSize(60, 150);
            //Setting a graphic to the button
            characterButton.setGraphic(characterImgview);
            characterButton.setUserData(charCard.getAsset());
            characterButton.setOnAction(new EventHandler() {

                @Override public void handle(Event event) {
                    if(currentPhase!=GamePhase.ANOTHERPLAYERTURN)
                        chooseCharacter(event);
                }
            });
            List<String> characterStudentName = new ArrayList<>();
            characterStudentName.add("innkeeper.jpg");
            characterStudentName.add("clown.jpg");
            characterStudentName.add("princess.jpg");
            EnumMap<Color,Integer> studentsOnCard=new EnumMap<Color, Integer>(Color.class);
            if (characterStudentName.contains(charCard.getAsset())) {
                studentsOnCard=charCard.getStudents().get();
                for(Color c:Color.values()){
                    while(studentsOnCard.get(c)>0)
                    {
                        Image studentImg = new Image(getClass().getResourceAsStream("/graphics/board/" + c.getFileStudent()));
                        ImageView studentImgview = new ImageView(studentImg);
                        studentImgview.setFitHeight(15);
                        studentImgview.setPreserveRatio(true);
                        cardGridList.get(countForCardGrid).add(studentImgview,j,k);
                        if(j==1){
                            j=0;
                            k++;
                        }else
                            j++;
                        studentsOnCard.put(c,studentsOnCard.get(c)-1);
                    }
                }
            }


            if(charCard.getAsset().equals("herbalist.jpg")){

                    int noEntryTitles=charCard.getNoEntryTiles().get();
                    while(noEntryTitles>0)
                    {
                        Image noEntryImage = new Image(getClass().getResourceAsStream("/graphics/board/deny_island_icon.png"));
                        ImageView noEntryImageview = new ImageView(noEntryImage);
                        noEntryImageview.setFitHeight(15);
                        noEntryImageview.setPreserveRatio(true);
                        cardGridList.get(countForCardGrid).add(noEntryImageview,j,k);
                        if(j==1){
                            j=0;
                            k++;
                        }else
                            j++;

                        noEntryTitles--;
                    }
            }

            character.getChildren().add(characterButton);

            countForCardGrid++;
        }

    }

    public void showMoney(){
        Image wizardImg = new Image(getClass().getResourceAsStream("/graphics/wizards/"+gui.getVmodel().getClientPlayer().getWizard().getCutFile()));
        ImageView wizardImgview = new ImageView(wizardImg);
        wizardImgview.setFitHeight(70);
        wizardImgview.setPreserveRatio(true);
        Image circle = new Image(getClass().getResourceAsStream("/graphics/additionalElement/cerchi.png"));
        ImageView circleImageView = new ImageView(circle);
        circleImageView.setFitHeight(80);
        circleImageView.setPreserveRatio(true);
        moneyWizardGrid.add(circleImageView,0,0);

        wizardGrid.add(wizardImgview,0,0);

        for(int i=0;i<gui.getVmodel().getClientPlayer().getMoney();i++)
        {
            Image moneyImg = new Image(getClass().getResourceAsStream("/graphics/additionalElement/coin.png"));
            ImageView moneyImgView=new ImageView(moneyImg);
            moneyImgView.setFitHeight(70);
            moneyImgView.setPreserveRatio(true);
            moneyWizardGrid.add(moneyImgView,i+1,0);
        }

    }

    public void chooseCharacter(Event event){
        currentPhase=GamePhase.CHARACTER;
        Node node=(Node)event.getSource();
        String asset=(String) node.getUserData();


        switch (asset){
            case "innkeeper.jpg":

                break;

        }
        //gui.send(new UseCharacterCard());

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
        int count=3;
        for(Cloud cloud:gui.getVmodel().getClouds()){
            cloudGrids.getChildren().get(count).setUserData(count);
            cloudGrids.getChildren().get(count).addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                @Override public void handle(MouseEvent mouseEvent) {

                    if(currentPhase==GamePhase.CHOOSECLOUD){
                        chooseCloud(mouseEvent);
                    }
                }
            });
            cloudGridsList.add((GridPane)cloudGrids.getChildren().get(count));
            count++;
        }

        schoolEntranceGridsList.add((GridPane)mySchoolPane.getChildren().get(1));
        schoolEntranceGridsList.add((GridPane)secondSchoolPane.getChildren().get(1));
        schoolDiningGridsList.add((GridPane)mySchoolPane.getChildren().get(2));
        schoolDiningGridsList.add((GridPane)secondSchoolPane.getChildren().get(2));
        schoolTowersGridsList.add((GridPane)mySchoolPane.getChildren().get(3));
        schoolTowersGridsList.add((GridPane)secondSchoolPane.getChildren().get(3));
        if(gui.getVmodel().getPlayers().size()==3){
            schoolEntranceGridsList.add((GridPane)thirdSchoolPane.getChildren().get(1));
            schoolDiningGridsList.add((GridPane)thirdSchoolPane.getChildren().get(2));
            schoolTowersGridsList.add((GridPane)thirdSchoolPane.getChildren().get(3));
        }

        for(int i=0; i<gui.getVmodel().getIslands().size();i++){
            islandGroupsList.add((Group)boardAndOthersSchool.getChildren().get(i));
            islandGridsList.add((GridPane)islandGroupsList.get(i).getChildren().get(1));
        }


    }
    public void showBoard(){
        this.showCloud();
        this.showSchool();
        this.showIsland();
        this.showCharacterCard();
        this.showMoney();
    }

    public void showIsland() {
        VirtualModel vmodel=gui.getVmodel();
        int count=0;
        for(Island island: vmodel.getIslands()){
            int k=0;
            int j=0;
            for(Color color:island.getStudents().keySet()){
                for(int i=0; i<island.getStudents().get(color);i++){
                    Image studentImg = new Image(getClass().getResourceAsStream("/graphics/board/" + color.getFileStudent()));
                    ImageView studentImgview = new ImageView(studentImg);
                    studentImgview.setFitHeight(15);
                    studentImgview.setPreserveRatio(true);
                    islandGridsList.get(count).add(studentImgview,j,k);
                    Group actualGroup=islandGroupsList.get(count);
                    actualGroup.setUserData(count);
                        islandGroupsList.get(count).addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                            @Override public void handle(MouseEvent mouseEvent) {

                                if(currentPhase==GamePhase.CHARACTER){
                                    posIsland=(Integer) actualGroup.getUserData();
                                    System.out.println("posizione isola: "+posIsland);
                                }
                            }
                        });

                    if(j==3){
                        k++;
                        j=0;
                    }else j++;
                }
            }
            count++;
        }
    }

    public void showSchool(){
        VirtualModel vmodel=gui.getVmodel();
        int count=0;

        for(Player player: vmodel.getPlayers()){
            int k=0;
            int j=0;
            /* show entrance students*/
            for(Color color: player.getEntryStudents().keySet()) {
                for (int i = 0; i < player.getEntryStudents().get(color); i++) {
                    Image studentImg = new Image(getClass().getResourceAsStream("/graphics/board/" + color.getFileStudent()));
                    ImageView studentImgview = new ImageView(studentImg);
                    studentImgview.setFitHeight(15);
                    //wizardImgview.setFitWidth(494);
                    studentImgview.setPreserveRatio(true);
                    if(count == 0){
                        studentImgview.setUserData(new Integer[]{j, k});
                        studentImgview.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                            @Override public void handle(MouseEvent mouseEvent) {
                                //Evidenziare lo studente e settare la variabile
                                setSelectedStudent(mouseEvent);
                            }
                        });
                    }
                    schoolEntranceGridsList.get(count).add(studentImgview, j, k);
                    if (j == 3 && k==0) {
                        k++;
                        j = 0;
                    } else {
                        j++;
                    }
                }
            }
            /* show towers on school*/
            k=0;
            j=0;
            for(int h=0; h<player.getNumberOfTower();h++){
                Image towerImg = new Image(getClass().getResourceAsStream("/graphics/board/" + player.getTower().getFile()));
                ImageView towerImgView = new ImageView(towerImg);
                towerImgView.setFitHeight(30);
                towerImgView.setRotate(90);
                towerImgView.setPreserveRatio(true);
                schoolTowersGridsList.get(count).add(towerImgView,j,k);
                if (j == 3) {
                    k++;
                    j = 0;
                } else {
                    j++;
                }
            }

            /* show dining students*/
            for(Color color: player.getStudents().keySet()) {
                j=0;
                for (int i = 0; i < player.getStudents().get(color); i++) {
                    Image studentImg = new Image(getClass().getResourceAsStream("/graphics/board/" + color.getFileStudent()));
                    ImageView studentImgview = new ImageView(studentImg);
                    studentImgview.setFitHeight(15);
                    studentImgview.setPreserveRatio(true);
                    switch(color){
                        case BLUE:
                            schoolDiningGridsList.get(count).add(studentImgview,0,j);
                            break;
                        case PINK:
                            schoolDiningGridsList.get(count).add(studentImgview,1,j);
                            break;
                        case YELLOW:
                            schoolDiningGridsList.get(count).add(studentImgview,2,j);
                            break;
                        case RED:
                            schoolDiningGridsList.get(count).add(studentImgview,3,j);
                            break;
                        case GREEN:
                            schoolDiningGridsList.get(count).add(studentImgview,4,j);
                            break;
                    }
                    j++;
                }
                }
            count++;
        }



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
        character.setVisible(false);
        wizardGrid.setVisible(false);
        moneyWizardGrid.setVisible(false);
        cardGrid1.setVisible(false);
        cardGrid2.setVisible(false);
        cardGrid3.setVisible(false);
        for(int i=0;i<3;i++){
            character.getChildren().get(i).setDisable(true);
        }

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

    public void setCurrentPhase(GamePhase currentPhase) {
        this.currentPhase = currentPhase;
    }


    public void setSelectedStudent(MouseEvent event){
        System.out.println(currentPhase);
        if(currentPhase.equals(GamePhase.MOVESTUDENT)){
            if(selectedStudent != null){
                for (Node student : this.schoolEntranceGridsList.get(0).getChildren()) {
                    if(GridPane.getColumnIndex(student) ==  selectedStudent[0] && GridPane.getRowIndex(student) == selectedStudent[1]){
                        student.setEffect(new DropShadow(0, javafx.scene.paint.Color.DARKORANGE));
                    }
                }
            }
            Node node = (Node) event.getSource();
            this.selectedStudent = (Integer[]) node.getUserData();

            for (Node student : this.schoolEntranceGridsList.get(0).getChildren()) {
                if(GridPane.getColumnIndex(student) ==  selectedStudent[0] && GridPane.getRowIndex(student) == selectedStudent[1]){
                    student.setEffect(new DropShadow(BlurType.GAUSSIAN, javafx.scene.paint.Color.DARKORANGE, 15, 0.7, 0, 0 ));
                }
            }

        }

    }
}
