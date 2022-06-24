package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.ClientToServer.*;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.GUI.GamePhase;
import it.polimi.ingsw.Client.VirtualModel;
import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Server.ServerToClient.SelectAssistantCard;
import it.polimi.ingsw.Server.ServerToClient.SelectWizard;
import it.polimi.ingsw.Server.ServerToClient.UpdateMessage;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

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
    public HBox hboxColorCharacter;
    public HBox charAndMoney;
    public AnchorPane secondSchoolPane;
    public AnchorPane thirdSchoolPane;
    public AnchorPane mySchoolPane;
    public ImageView wizardImg;
    public ImageView moneyImg;
    public Text nickname;
    public Text money;
    public Group wizardAndMoney;

    public List<GridPane> cloudGridsList=new ArrayList<>();
    public List<GridPane> schoolEntranceGridsList=new ArrayList<>();
    public List<GridPane> schoolDiningGridsList=new ArrayList<>();

    public List<GridPane> schoolProfGridsList=new ArrayList<>();
    public List<GridPane> schoolTowersGridsList=new ArrayList<>();
    private Integer[] selectedStudent = null;
    private Color colorOfSelectedStudent = null;
    private int numberOfMovedStudent = 0;

    private List<GridPane> islandGridsList=new ArrayList<>();
    private List<GridPane> islandTowerGridsList=new ArrayList<>();
    private List<Group> islandGroupsList=new ArrayList<>();
    private List<GridPane> cardGridList=new ArrayList<>();
    public GridPane cardGrid1;
    public GridPane cardGrid2;
    public GridPane cardGrid3;

    public Button characterButton;

    private boolean firsTimeInMethod=true;

    private Integer posIsland=null;
    private ImageView imageSelectedIsland = null;
    private EnumMap<Color,Integer> choosenStudent=null;
    private EnumMap<Color,Integer> entranceStudent=null;
    private Color color=null;
    private List<Node> selectedNodes=new ArrayList<Node>();
    private int numOfStudentChoose=0;
    private boolean islandCanSelect = false;
    private int numOfEntranceStudChoose=0;


    public void initialize() {
        mainPane.setVisible(true);
        boardAndOthersSchool.setVisible(false);
        mySchool.setVisible(false);
        wizardAndMoney.setVisible(false);
        listOfWizards.setVisible(false);
        selectWizard.setVisible(false);
        waitForOtherWizard.setVisible(false);
        wizardAndMoney.setVisible(false);
        characterButton.setVisible(false);
        hboxColorCharacter.setVisible(false);
        currentPhase = GamePhase.WIZARD;
        for (Group islandGroup: islandGroupsList) {
            ImageView image = (ImageView) islandGroup.getChildren().get(0);
            image.setUserData(image);
            image.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent event) {
                    islandClicked(event);
                }
            });
        }
    }

    public void islandCliked(Event event){
        if(currentPhase == GamePhase.CHARACTER && islandCanSelect){
            if(imageSelectedIsland != null){
                imageSelectedIsland.setEffect(new DropShadow(0, javafx.scene.paint.Color.DARKORANGE));
            }
            Node node = (Node) event.getSource();
            imageSelectedIsland = (ImageView) node.getUserData();
            posIsland = Integer.parseInt(imageSelectedIsland.getId().substring(6,7));
            System.out.println(posIsland);
            imageSelectedIsland.setEffect(new DropShadow(BlurType.GAUSSIAN, javafx.scene.paint.Color.DARKORANGE, 15, 0.7, 0, 0 ));
        }
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
                wizardAndMoney.setVisible(true);
                character.setVisible(true);
                cardGrid1.setVisible(true);
                cardGrid2.setVisible(true);
                cardGrid3.setVisible(true);
                for(int i=0;i<3;i++){
                    character.getChildren().get(i).setDisable(false);
                }
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

    public void showCharacterCard(){
        character.getChildren().clear();
        List<CharacterCard> characterCards=gui.getVmodel().getCharacterCards();

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
                        currentPhase=GamePhase.CHARACTER;
                    showCharacterOptions(event);
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

                        studentImgview.setUserData(c);
                        studentImgview.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                            @Override public void handle(MouseEvent mouseEvent) {
                               setValueForCharacterCard(mouseEvent);
                            }
                        });
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

    public void setWizardAndNickname(){
        wizardImg.setImage(new Image(getClass().getResourceAsStream(
                "/graphics/wizards/" + gui.getVmodel().getClientPlayer().getWizard().getCutFile())));
        wizardImg.setFitHeight(80);
        wizardImg.setPreserveRatio(true);
        moneyImg.setImage(new Image(getClass().getResourceAsStream("/graphics/additionalElement/coin.png")));
        moneyImg.setFitHeight(70);
        moneyImg.setPreserveRatio(true);
        firsTimeInMethod=false;
        money.setText(String.valueOf(gui.getVmodel().getClientPlayer().getMoney()));
        nickname.setText(gui.getVmodel().getClientPlayer().getNickname());
        wizardAndMoney.setVisible(true);
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
            cloudGrids.getChildren().get(count).setUserData(count-3);
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


        mySchoolPane.getChildren().get(2).addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                schoolClicked(mouseEvent);
            }
        });
        count=1;
        for(int i=0; i<gui.getVmodel().getPlayers().size();i++){
            if(gui.getVmodel().getPlayers().get(i).equals(gui.getVmodel().getClientPlayer())) {
                schoolEntranceGridsList.add((GridPane) mySchoolPane.getChildren().get(1));
                schoolDiningGridsList.add((GridPane) mySchoolPane.getChildren().get(2));
                schoolTowersGridsList.add((GridPane) mySchoolPane.getChildren().get(3));
                schoolProfGridsList.add((GridPane)mySchoolPane.getChildren().get(4));
            }else{
                count++;
                switch(count){
                    case 2:
                        schoolEntranceGridsList.add((GridPane)secondSchoolPane.getChildren().get(1));
                        schoolDiningGridsList.add((GridPane)secondSchoolPane.getChildren().get(2));
                        schoolTowersGridsList.add((GridPane)secondSchoolPane.getChildren().get(3));
                        schoolProfGridsList.add((GridPane)secondSchoolPane.getChildren().get(4));
                        break;
                    case 3:
                        schoolEntranceGridsList.add((GridPane)thirdSchoolPane.getChildren().get(1));
                        schoolDiningGridsList.add((GridPane)thirdSchoolPane.getChildren().get(2));
                        schoolTowersGridsList.add((GridPane)thirdSchoolPane.getChildren().get(3));
                        schoolProfGridsList.add((GridPane)thirdSchoolPane.getChildren().get(4));
                }
            }
        }
        for(int i=0; i<gui.getVmodel().getIslands().size();i++){
            islandGroupsList.add((Group)boardAndOthersSchool.getChildren().get(i));
            islandGridsList.add((GridPane)islandGroupsList.get(i).getChildren().get(1));
            islandTowerGridsList.add((GridPane)islandGroupsList.get(i).getChildren().get(2));
        }
        cardGridList.add(cardGrid1);
        cardGridList.add(cardGrid2);
        cardGridList.add(cardGrid3);


    }
    public void showBoard(){
        for(GridPane gridPane: schoolEntranceGridsList)
            gridPane.getChildren().clear();
        for(GridPane gridPane: schoolDiningGridsList)
            gridPane.getChildren().clear();
        for(GridPane gridPane: schoolTowersGridsList)
            gridPane.getChildren().clear();
        for(GridPane gridPane: islandGridsList)
            gridPane.getChildren().clear();
        for(GridPane gridPane: cloudGridsList)
            gridPane.getChildren().clear();
        for(GridPane gridPane: schoolProfGridsList)
            gridPane.getChildren().clear();
        for(GridPane gridPane: islandTowerGridsList)
            gridPane.getChildren().clear();
        for(GridPane gridPane: cardGridList)
            gridPane.getChildren().clear();


        this.showCloud();
        this.showSchool();
        this.showIsland();
        this.showCharacterCard();
        this.setWizardAndNickname();
    }

    public void showIsland() {
        VirtualModel vmodel=gui.getVmodel();
        int count=0;
        for(Island island: vmodel.getIslands()){
            if(vmodel.getMotherNaturePosition()==count){
                Image motherNatureImg=new Image(getClass().getResourceAsStream("/graphics/icons/mother_nature.png"));
                ImageView motherNatureImgview = new ImageView(motherNatureImg);
                motherNatureImgview.setFitHeight(20);
                motherNatureImgview.setPreserveRatio(true);
                motherNatureImgview.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent mouseEvent) {
                        setSelectedMotherNature(mouseEvent);
                    }
                });
                islandGridsList.get(count).add(motherNatureImgview,3,3);
            }
            int k=0;
            int j=0;
            Group actualGroup=islandGroupsList.get(count);
            actualGroup.setUserData(count);

            islandGridsList.get(count).setUserData(count);
            islandGridsList.get(count).addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                @Override public void handle(MouseEvent mouseEvent) {

                    islandClicked(mouseEvent);
                    if(currentPhase==GamePhase.CHARACTER){
                        //setValueForCharacterCard(actualGroup);
                    }
                }
            });
            for(Color color:island.getStudents().keySet()){
                for(int i=0; i<island.getStudents().get(color);i++){
                    Image studentImg = new Image(getClass().getResourceAsStream("/graphics/board/" + color.getFileStudent()));
                    ImageView studentImgview = new ImageView(studentImg);
                    studentImgview.setFitHeight(15);
                    studentImgview.setPreserveRatio(true);
                    islandGridsList.get(count).add(studentImgview,j,k);
                    if(j==3){
                        k++;
                        j=0;
                    }else j++;
                }
            }
            if(island.getNumberOfTowers()>0){
                Image towerImg = new Image(getClass().getResourceAsStream("/graphics/board/" +island.getTower().get().getFile()));
                ImageView towerImgview = new ImageView(towerImg);
                towerImgview.setFitHeight(20);
                towerImgview.setPreserveRatio(true);
                islandTowerGridsList.get(count).add(towerImgview,0,0);
                Text numberOfTower=new Text(": "+island.getNumberOfTowers());
                numberOfTower.setFont(new Font(20));
                islandTowerGridsList.get(count).add(numberOfTower,1,0);
            }
            count++;
        }
    }


    public void islandClicked(MouseEvent event){
        Node node = (Node) event.getSource();
        if(currentPhase == GamePhase.MOVESTUDENT && selectedStudent!=null){
            System.out.println((int) node.getUserData());
            System.out.println(colorOfSelectedStudent);
            System.out.println(selectedStudent[0]+" "+selectedStudent[1]);
            gui.send(new MoveStudent(MoveTo.ISLAND,colorOfSelectedStudent,(int) node.getUserData(),gui.getVmodel().getNumOfInstance()));
            colorOfSelectedStudent = null;
            selectedStudent = null;
            if(++numberOfMovedStudent == 3){
                numberOfMovedStudent = 0;
                currentPhase = GamePhase.GAME;
            }
        }else if(currentPhase== GamePhase.MOVEMOTHERNATURE && gui.getVmodel().getMotherNaturePosition()!=((int)node.getUserData())){
            System.out.println("user data:"+((int)node.getUserData()));
            gui.send(new MoveMotherNature((int)(node.getUserData())-gui.getVmodel().getMotherNaturePosition()));
            currentPhase = GamePhase.GAME;
        }
    }

    public void schoolClicked(MouseEvent event){
        if(currentPhase == GamePhase.MOVESTUDENT && selectedStudent!=null){
            Node node = (Node) event.getSource();
            System.out.println(colorOfSelectedStudent);
            System.out.println(selectedStudent[0]+" "+selectedStudent[1]);
            gui.send(new MoveStudent(MoveTo.SCHOOL,colorOfSelectedStudent,gui.getVmodel().getNumOfInstance()));
            colorOfSelectedStudent = null;
            selectedStudent = null;
            if(++numberOfMovedStudent == 3){
                numberOfMovedStudent = 0;
                currentPhase = GamePhase.GAME;
            }
        }
    }

    public void showCharacterOptions(Event event){
        Node card= (Node) event.getSource();
        String asset=(String) card.getUserData();
        characterButton.setVisible(true);
        characterButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                gui.send(new UseCharacterCard(asset,posIsland,choosenStudent,entranceStudent,color));
                characterButton.setVisible(false);
                currentPhase = GamePhase.GAME;
                posIsland=null;
                choosenStudent=null;
                entranceStudent=null;
                color=null;
            }
        });

        switch (asset) {
            case "innkeeper.jpg":
                islandCanSelect=true;
                break;
            case "clown.jpg":
            case "princess.jpg":

                break;
            case "auctioneer.jpg":
            case "herbalist.jpg":
                //fare scegliere l'isola
                islandCanSelect=true;
                break;
            case "lumberjack.jpg":
            case "thief.jpg":
                //choose the color not to be considered
                //choose the color to put back in the bag
                for(Color c: Color.values()){
                Image studentImg = new Image(getClass().getResourceAsStream("/graphics/board/" + c.getFileStudent()));
                ImageView studentImgview = new ImageView(studentImg);
                studentImgview.setFitHeight(15);
                studentImgview.setPreserveRatio(true);

                studentImgview.setUserData(c);
                studentImgview.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent mouseEvent) {
                        color=c;
                    }
                });
                hboxColorCharacter.getChildren().add(studentImgview);
                }
                hboxColorCharacter.setVisible(true);
                break;
            case "storyteller.jpg":
                //scambio scuola entrata

            default:
                // case "merchant.jpg":
                //case "postman.jpg":
                //case "centaur.jpg":
                //case "infantryman.jpg":
            break;
        }

    }
    public void setValueForCharacterCard(MouseEvent event){

           if(numOfStudentChoose==0){
               choosenStudent=new EnumMap<Color, Integer>(Color.class);
               for(Color c:Color.values())
                   choosenStudent.put(c,0);
               numOfStudentChoose++;
           }
           ImageView actualImageStudent=(ImageView) event.getSource();
           actualImageStudent.setEffect(new DropShadow(BlurType.GAUSSIAN, javafx.scene.paint.Color.DARKORANGE, 15, 0.7, 0, 0 ));
           Color choosenColor=(Color)actualImageStudent.getUserData();
           choosenStudent.put(choosenColor,choosenStudent.get(choosenColor)+1);




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
                    if(player.equals(vmodel.getClientPlayer())){
                        studentImgview.setUserData(new Object[]{j, k, color});
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

            /*show professors*/
            for(Color color: Color.values()){
                if(player.isPresentProfessor(color)){
                    Image profImg = new Image(getClass().getResourceAsStream("/graphics/board/" + color.getFileTeacher()));
                    ImageView profImgview = new ImageView(profImg);
                    profImgview.setFitHeight(15);
                    profImgview.setPreserveRatio(true);
                    switch(color){
                        case BLUE:
                            schoolProfGridsList.get(count).add(profImgview,0,0);
                            break;
                        case PINK:
                            schoolProfGridsList.get(count).add(profImgview,1,0);
                            break;
                        case YELLOW:
                            schoolProfGridsList.get(count).add(profImgview,2,0);
                            break;
                        case RED:
                            schoolProfGridsList.get(count).add(profImgview,3,0);
                            break;
                        case GREEN:
                            schoolProfGridsList.get(count).add(profImgview,4,0);
                            break;
                    }
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
        wizardAndMoney.setVisible(false);
        character.setVisible(false);
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
        if(currentPhase==GamePhase.CHOOSECLOUD){
            Node node=(Node) event.getSource();
            int cloudId=(int) node.getUserData();
            System.out.println("cloudId"+cloudId);
            gui.send((new ChooseCloud(cloudId)));
        }
    }

    public void setCurrentPhase(GamePhase currentPhase) {
        this.currentPhase = currentPhase;
    }


    public void setSelectedStudent(MouseEvent event){
        System.out.println(currentPhase);
        if(currentPhase.equals(GamePhase.MOVESTUDENT)){
            if(selectedStudent != null){
                for (Node student : ((GridPane)this.mySchoolPane.getChildren().get(1)).getChildren()) {
                    if(GridPane.getColumnIndex(student) ==  selectedStudent[0] && GridPane.getRowIndex(student) == selectedStudent[1]){
                        student.setEffect(new DropShadow(0, javafx.scene.paint.Color.DARKORANGE));
                    }
                }
            }
            Node node = (Node) event.getSource();
            Object[] data = (Object[]) node.getUserData();
            this.selectedStudent = new Integer[]{(Integer) data[0], (Integer) data[1]};
            this.colorOfSelectedStudent = (Color) data[2];
            for (Node student : ((GridPane)this.mySchoolPane.getChildren().get(1)).getChildren()) {
                if(GridPane.getColumnIndex(student) ==  selectedStudent[0] && GridPane.getRowIndex(student) == selectedStudent[1]){
                    student.setEffect(new DropShadow(BlurType.GAUSSIAN, javafx.scene.paint.Color.DARKORANGE, 15, 0.7, 0, 0 ));
                }
            }

        }
        if(currentPhase.equals(GamePhase.CHARACTER)){
            if(numOfEntranceStudChoose==0){
                entranceStudent=new EnumMap<Color, Integer>(Color.class);
                for(Color c:Color.values())
                    entranceStudent.put(c,0);
                numOfEntranceStudChoose++;
            }
            ImageView actualImageStudent=(ImageView) event.getSource();
            Object[] data = (Object[]) actualImageStudent.getUserData();
            Color choosenColor=(Color)data[2];
            actualImageStudent.setEffect(new DropShadow(BlurType.GAUSSIAN, javafx.scene.paint.Color.DARKORANGE, 15, 0.7, 0, 0 ));
            entranceStudent.put(choosenColor,entranceStudent.get(choosenColor)+1);
        }
    }

    public void setSelectedMotherNature(MouseEvent event){
        if(currentPhase.equals(GamePhase.MOVEMOTHERNATURE)){
            Node node = (Node) event.getSource();
            node.setEffect(new DropShadow(0, javafx.scene.paint.Color.DARKORANGE));
            node.setEffect(new DropShadow(BlurType.GAUSSIAN, javafx.scene.paint.Color.DARKORANGE, 15, 0.7, 0, 0 ));
        }
    }

    public void handleMerge(UpdateMessage msg){
        int conqueredIsland=msg.getChange().getConquerIsland();
        int mergedIsland1=msg.getChange().getMergedIsland1();

        double x1 = islandGroupsList.get(mergedIsland1).getLayoutX();
        double x2 = islandGroupsList.get(conqueredIsland).getLayoutX();
        double y1 = islandGroupsList.get(mergedIsland1).getLayoutY();
        double y2 = islandGroupsList.get(conqueredIsland).getLayoutY();
        if(null!=msg.getChange().getMergedIsland2()){
            int mergedIsland2=msg.getChange().getMergedIsland2();
            double x3 = islandGroupsList.get(mergedIsland2).getLayoutX();
            double y3 = islandGroupsList.get(mergedIsland2).getLayoutY();
            double x=(x1+x2+x3)/3;
            double y=(y1+y2+y3)/3;
            if(mergedIsland1<mergedIsland2){
                islandGridsList.remove(mergedIsland2);
                islandGroupsList.get(mergedIsland2).getChildren().clear();
                islandGroupsList.remove(mergedIsland2);
                islandGroupsList.get(mergedIsland1).setLayoutX(x);
                islandGroupsList.get(mergedIsland1).setLayoutY(y);
                //islandGroupsList.get(mergedIsland1).setScaleX( islandGroupsList.get(mergedIsland1).getScaleX()*1.2);
                //islandGroupsList.get(mergedIsland1).setScaleY( islandGroupsList.get(mergedIsland1).getScaleY()*1.2);
            }else{
                islandGridsList.remove(mergedIsland1);
                islandGroupsList.get(mergedIsland1).getChildren().clear();
                islandGroupsList.remove(mergedIsland1);
                islandGroupsList.get(mergedIsland2).setLayoutX(x);
                islandGroupsList.get(mergedIsland2).setLayoutY(y);
                //islandGroupsList.get(mergedIsland1).setScaleX( islandGroupsList.get(mergedIsland2).getScaleX()*1.2);
                //islandGroupsList.get(mergedIsland1).setScaleY( islandGroupsList.get(mergedIsland2).getScaleY()*1.2);
            }
            islandGridsList.remove(conqueredIsland);
            islandGroupsList.get(conqueredIsland).getChildren().clear();
            islandGroupsList.remove(conqueredIsland);
        }else{
            double x=(x1+x2)/2;
            double y=(y1+y2)/2;
            if(mergedIsland1<conqueredIsland){
                islandGridsList.remove(conqueredIsland);
                islandGroupsList.get(conqueredIsland).getChildren().clear();
                islandGroupsList.remove(conqueredIsland);
                islandGroupsList.get(mergedIsland1).setLayoutX(x);
                islandGroupsList.get(mergedIsland1).setLayoutY(y);
                //islandGroupsList.get(mergedIsland1).setScaleX( islandGroupsList.get(mergedIsland1).getScaleX()*1.1);
                //islandGroupsList.get(mergedIsland1).setScaleY( islandGroupsList.get(mergedIsland1).getScaleY()*1.1);
            }else{
                islandGridsList.remove(mergedIsland1);
                islandGroupsList.get(mergedIsland1).getChildren().clear();
                islandGroupsList.remove(mergedIsland1);
                islandGroupsList.get(conqueredIsland).setLayoutX(x);
                islandGroupsList.get(conqueredIsland).setLayoutY(y);
                //islandGroupsList.get(conqueredIsland).setScaleX( islandGroupsList.get(conqueredIsland).getScaleX()*1.1);
                //islandGroupsList.get(conqueredIsland).setScaleY( islandGroupsList.get(conqueredIsland).getScaleY()*1.1);
            }
        }
        //System.out.println(islandGroupsList);
    }
}
