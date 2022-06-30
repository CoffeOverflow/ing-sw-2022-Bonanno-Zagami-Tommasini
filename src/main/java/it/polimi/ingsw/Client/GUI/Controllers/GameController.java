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
import java.util.HashMap;
import java.util.List;

/***
 * Controller for main Game phase
 * @author Giuseppe Bonanno, Federica Tommasini, Angelo Zagami
 * @see GUIController
 */
public class GameController implements GUIController{
    private GUI gui;
    private GamePhase currentPhase;
    @FXML private AnchorPane mainPane;
    @FXML private VBox selectWizard;
    @FXML private VBox waitForOtherWizard;
    @FXML private HBox listOfWizards;

    @FXML private AnchorPane boardAndOthersSchool;

    @FXML private ImageView thirdPlayerSchool;
    @FXML private ImageView cloud3;
    @FXML private ImageView mySchool;

    @FXML private AnchorPane cloudGrids;
    @FXML private HBox assistantCard;
    @FXML private HBox character;
    @FXML private HBox hboxColorCharacter;
    @FXML private HBox hboxColorCharacter1;
    @FXML private AnchorPane secondSchoolPane;
    @FXML private AnchorPane thirdSchoolPane;
    @FXML private AnchorPane mySchoolPane;
    @FXML private ImageView wizardImg;
    @FXML private ImageView moneyImg;
    @FXML private Text nickname;
    @FXML private Text money;
    @FXML private Group wizardAndMoney;

    private List<GridPane> cloudGridsList=new ArrayList<>();
    private List<GridPane> schoolEntranceGridsList=new ArrayList<>();
    private List<GridPane> schoolDiningGridsList=new ArrayList<>();

    private List<GridPane> schoolProfGridsList=new ArrayList<>();
    private List<GridPane> schoolTowersGridsList=new ArrayList<>();
    private Integer[] selectedStudent = null;
    private Color colorOfSelectedStudent = null;
    private int numberOfMovedStudent = 0;

    private List<GridPane> islandGridsList=new ArrayList<>();
    private List<GridPane> islandTowerGridsList=new ArrayList<>();
    private List<Group> islandGroupsList=new ArrayList<>();
    private  List<GridPane> cardGridList=new ArrayList<>();
    @FXML private GridPane cardGrid1;
    @FXML private GridPane cardGrid2;
    @FXML private GridPane cardGrid3;

    @FXML private Button characterButton;

    private boolean firsTimeInMethod=true;

    private Integer posIsland=null;
    private ImageView imageSelectedIsland = null;
    private EnumMap<Color,Integer> choosenStudent=null;
    private EnumMap<Color,Integer> entranceStudent=null;
    private Color color=null;
    private int numOfStudentChoose=0;
    private boolean islandCanSelect = false;
    private int numOfEntranceStudChoose=0;
    private int colorSelected=0;
    private String asset;
    @FXML private ImageView player2Wizard;
    @FXML private ImageView player3Wizard;
    @FXML private Text player2Nickname;
    @FXML private Text player3Nickname;
    @FXML private ImageView player1Assistant;
    @FXML private ImageView player2Assistant;
    @FXML private ImageView player3Assistant;

    private HashMap<Integer,Integer> vModelPosGuiPos=new HashMap<>();

    private boolean playedCharacterCard=false;


    /***
     * Method automatically called when the scene is loaded.
     * Initializes the scene.
     */
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
        player1Assistant.setVisible(false);
        currentPhase = GamePhase.WIZARD;
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    /***
     * Method used to show and hide the students and tower on the school in select assistant card phase
     * @param show True if the students and towers have to show, false otherwise
     */
    public void showStudentsAndTowers(boolean show){
        schoolEntranceGridsList.get((vModelPosGuiPos.get(0))).setVisible(show);
        schoolDiningGridsList.get((vModelPosGuiPos.get(0))).setVisible(show);
        schoolProfGridsList.get((vModelPosGuiPos.get(0))).setVisible(show);
        schoolTowersGridsList.get((vModelPosGuiPos.get(0))).setVisible(show);
        player1Assistant.setVisible(show);

    }

    /***
     * Method for change phase depending on the different phases
     */
    public void changePhase(){
        switch (currentPhase){
            case WIZARD:
                selectWizard.setVisible(false);
                waitForOtherWizard.setVisible(true);
                mySchool.setVisible(true);
                currentPhase = GamePhase.GAME;
                break;

            case ASSISTANT:
                assistantCard.setVisible(false);
                mySchool.setVisible(true);
                showStudentsAndTowers(true);
                wizardAndMoney.setVisible(true);
                character.setVisible(true);
                cardGrid1.setVisible(true);
                cardGrid2.setVisible(true);
                cardGrid3.setVisible(true);
                if (null!=gui.getVmodel().getCharacterCards()){
                    for(int i=0;i<3;i++){
                        character.getChildren().get(i).setDisable(false);
                    }
                }
                currentPhase = GamePhase.GAME;

                break;
        }
    }

    /***
     * Change the game phase with the one passed
     * @param phase The new game phase
     */
    public void changePhase(GamePhase phase){
        currentPhase = phase;
    }

    /***
     * Method used to show the assistant card played by other players
     * @param playerID The player ID
     * @param card The card
     */
    public void playerPlayAssistantCard(int playerID, AssistantCard card){
        if(gui.getVmodel().getPlayers().get(vModelPosGuiPos.get(1)).getPlayerID() == playerID){
            player2Assistant.setImage(new Image(getClass().getResourceAsStream(card.getAsset())));
        }
        if(gui.getVmodel().getPlayers().size()>2){
            if(gui.getVmodel().getPlayers().get(vModelPosGuiPos.get(2)).getPlayerID() == playerID){
                player3Assistant.setImage(new Image(getClass().getResourceAsStream(card.getAsset())));
            }
        }
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

    /***
     * Show the Character cards on the screen with the students and no entry cards
     */
    public void showCharacterCard(){
        List<CharacterCard> characterCards=gui.getVmodel().getCharacterCards();
        if(firsTimeInMethod){
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
                        if(currentPhase!=GamePhase.ANOTHERPLAYERTURN && !playedCharacterCard)
                        {   currentPhase=GamePhase.CHARACTER;
                            showCharacterOptions(event);
                    }}
                });
                character.getChildren().add(characterButton);
            }
            firsTimeInMethod=false;
        }
        int countForCardGrid=0;
        for(CharacterCard charCard:characterCards){
            int j=0;
            int k=0;
            List<String> characterStudentName = new ArrayList<>();
            characterStudentName.add("innkeeper.jpg");
            characterStudentName.add("clown.jpg");
            characterStudentName.add("princess.jpg");
            EnumMap<Color,Integer> studentsOnCard=new EnumMap<Color, Integer>(Color.class);
            if (characterStudentName.contains(charCard.getAsset())) {
                studentsOnCard=charCard.getStudents().get().clone();
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
                               if(currentPhase==GamePhase.CHARACTER)
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

            countForCardGrid++;
        }

    }

    /***
     * Show Wizards, Nickname and money of the players
     */
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
        if(gui.getVmodel().getCharacterCards() == null){
            money.setVisible(false);
            moneyImg.setVisible(false);
        }
        nickname.setText(gui.getVmodel().getClientPlayer().getNickname());
        wizardAndMoney.setVisible(true);
        player2Nickname.setText(gui.getVmodel().getPlayers().get(vModelPosGuiPos.get(1)).getNickname());
        player2Wizard.setImage(new Image(getClass().getResourceAsStream("/graphics/wizards/"+(gui.getVmodel().getPlayers().get(vModelPosGuiPos.get(1)).getWizard().getCutFile()))));
        if(gui.getVmodel().getPlayers().size() > 2){
            player3Nickname.setText(gui.getVmodel().getPlayers().get(vModelPosGuiPos.get(2)).getNickname());
            player3Wizard.setImage(new Image(getClass().getResourceAsStream("/graphics/wizards/"+(gui.getVmodel().getPlayers().get(vModelPosGuiPos.get(2)).getWizard().getCutFile()))));
        }
        else{
            player3Wizard.setVisible(false);
            player3Nickname.setVisible(false);
        }

    }


    /***
     * Method used for select player's Wizard
     * @param wizards The list of Wizards available
     */
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

    /**
     * set up the grids to visualize the students on the board
     */
    public void setGrids(){
        waitForOtherWizard.setVisible(false);
        boardAndOthersSchool.setVisible(true);
        if(gui.getVmodel().getPlayers().size()==2){
            thirdPlayerSchool.setVisible(false);
            cloud3.setVisible(false);
        }
        /* set the grids for the students on the clouds*/
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

        count=0;
        for(int i=0; i<gui.getVmodel().getPlayers().size();i++){
            if(gui.getVmodel().getPlayers().get(i).equals(gui.getVmodel().getClientPlayer())) {
                /* set the grids for the students and the towers on the school of the player corresponding to the client */
                schoolEntranceGridsList.add((GridPane) mySchoolPane.getChildren().get(1));
                schoolDiningGridsList.add((GridPane) mySchoolPane.getChildren().get(2));
                schoolTowersGridsList.add((GridPane) mySchoolPane.getChildren().get(3));
                schoolProfGridsList.add((GridPane)mySchoolPane.getChildren().get(4));
                vModelPosGuiPos.put(0,i);
            }else{
                /* set the grids for the students and the towers of the other two players*/
                count++;
                switch(count){
                    case 1:
                        schoolEntranceGridsList.add((GridPane)secondSchoolPane.getChildren().get(1));
                        schoolDiningGridsList.add((GridPane)secondSchoolPane.getChildren().get(2));
                        schoolTowersGridsList.add((GridPane)secondSchoolPane.getChildren().get(3));
                        schoolProfGridsList.add((GridPane)secondSchoolPane.getChildren().get(4));
                        vModelPosGuiPos.put(1,i);
                        break;
                    case 2:
                        schoolEntranceGridsList.add((GridPane)thirdSchoolPane.getChildren().get(1));
                        schoolDiningGridsList.add((GridPane)thirdSchoolPane.getChildren().get(2));
                        schoolTowersGridsList.add((GridPane)thirdSchoolPane.getChildren().get(3));
                        schoolProfGridsList.add((GridPane)thirdSchoolPane.getChildren().get(4));
                        vModelPosGuiPos.put(2,i);
                        break;
                }
            }
        }
        /* set the grids for the students on the islands*/
        for(int i=0; i<gui.getVmodel().getIslands().size();i++){
            islandGroupsList.add((Group)boardAndOthersSchool.getChildren().get(i));
            islandGridsList.add((GridPane)islandGroupsList.get(i).getChildren().get(1));
            islandTowerGridsList.add((GridPane)islandGroupsList.get(i).getChildren().get(2));
        }

        /* set the grids for the students on the character cards*/
        cardGridList.add(cardGrid1);
        cardGridList.add(cardGrid2);
        cardGridList.add(cardGrid3);

       for (Group islandGroup: islandGroupsList) {
            islandGroup.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent event) {
                    islandClicked(event);
                }
            });

        }


    }

    /**
     * show the current instance of the board
     */
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
        if(null!=gui.getVmodel().getCharacterCards())
            this.showCharacterCard();
        this.setWizardAndNickname();
        player1Assistant.setVisible(true);
    }

    /**
     * place the students, the towers and the no entry tiles on the islands and visualize them
     */
    public void showIsland() {
        VirtualModel vmodel=gui.getVmodel();
        int count=0;
        for(Island island: vmodel.getIslands()){
            if(vmodel.getMotherNaturePosition()==count){
                System.out.println(vmodel.getMotherNaturePosition());
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
            int noEntryNumber=island.getNoEntryCard();
            while(noEntryNumber>0){
                Image noEntryImg = new Image(getClass().getResourceAsStream("/graphics/board/deny_island_icon.png"));
                ImageView noEntryImgview = new ImageView(noEntryImg);
                noEntryImgview.setFitHeight(15);
                noEntryImgview.setPreserveRatio(true);
                islandGridsList.get(count).add(noEntryImgview,j,k);
                if(j==3){
                    k++;
                    j=0;
                }else j++;
                noEntryNumber--;
            }
            Group actualGroup=islandGroupsList.get(count);
            actualGroup.setUserData(count);

            islandGridsList.get(count).setUserData(count);

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

    /***
     * Method used for select island in move Mothernature and character card phase
     * @param event The event associated to the island clicked
     */
    public void islandClicked(MouseEvent event){
        Node node = (Node) event.getSource();
        if(currentPhase == GamePhase.MOVESTUDENT && selectedStudent!=null){
            System.out.println((int) node.getUserData());
            System.out.println(colorOfSelectedStudent);
            System.out.println(selectedStudent[0]+" "+selectedStudent[1]);
            gui.send(new MoveStudent(MoveTo.ISLAND,colorOfSelectedStudent,(int) node.getUserData(),gui.getVmodel().getNumOfInstance()));
            colorOfSelectedStudent = null;
            selectedStudent = null;
            if(++numberOfMovedStudent == (gui.getVmodel().getPlayers().size() == 3 ? 4:3)){
                numberOfMovedStudent = 0;
                currentPhase = GamePhase.GAME;
            }
        }else if(currentPhase== GamePhase.MOVEMOTHERNATURE && gui.getVmodel().getMotherNaturePosition()!=((int)node.getUserData()) ){
            currentPhase = GamePhase.GAME;
            gui.send(new MoveMotherNature((int)(node.getUserData())-gui.getVmodel().getMotherNaturePosition()));
        }else if(currentPhase == GamePhase.CHARACTER && islandCanSelect){
            if(imageSelectedIsland != null){
                imageSelectedIsland.setEffect(new DropShadow(0, javafx.scene.paint.Color.DARKORANGE));
            }
            imageSelectedIsland = (ImageView) ((Group)node).getChildren().get(0);
            posIsland = (Integer) node.getUserData();
            imageSelectedIsland.setEffect(new DropShadow(BlurType.GAUSSIAN, javafx.scene.paint.Color.DARKORANGE, 15, 0.7, 0, 0 ));
        }
    }

    /***
     * Method used to manage click on the school in Move students phase
     * @param event The event associated to school
     */
    public void schoolClicked(MouseEvent event){
        if(currentPhase == GamePhase.MOVESTUDENT && selectedStudent!=null){
            Node node = (Node) event.getSource();
            gui.send(new MoveStudent(MoveTo.SCHOOL,colorOfSelectedStudent,gui.getVmodel().getNumOfInstance()));
            colorOfSelectedStudent = null;
            selectedStudent = null;
            if(++numberOfMovedStudent == 3){
                numberOfMovedStudent = 0;
                currentPhase = GamePhase.GAME;
            }
        }
    }

    /**
     * when the player clicks on a character card, a button to play the card appears,
     * after selecting the parameters related to the specific card the player can click
     * on the button and send the message to play the card
     * @param event click on the card
     */
    public void showCharacterOptions(Event event){
        Node card= (Node) event.getSource();
        asset=(String) card.getUserData();
        characterButton.setVisible(true);
        characterButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if(!playedCharacterCard) {
                    characterButton.setVisible(false);
                    hboxColorCharacter1.setVisible(false);
                    hboxColorCharacter.setVisible(false);
                    currentPhase = GamePhase.GAME;
                    gui.send(new UseCharacterCard(asset,posIsland,choosenStudent,entranceStudent,color));
                    playedCharacterCard=true;
                    if(null!=imageSelectedIsland)
                        imageSelectedIsland.setEffect(new DropShadow(0, javafx.scene.paint.Color.DARKORANGE));
                    imageSelectedIsland=null;
                    posIsland=null;
                    choosenStudent=null;
                    entranceStudent=null;
                    color=null;
                    colorSelected=0;
                    islandCanSelect=false;
                    numOfStudentChoose=0;
                    asset=null;
                }
            }
        });

        /*
         * if the card requires to select an island, a boolean variable is set to true in order to set the parameter
         * of the message when clicking on the island, if the card requires to select a color, it appears a list of
         * students from which the player can choose
         */
        switch (asset) {
            case "innkeeper.jpg":
            case "auctioneer.jpg":
            case "herbalist.jpg":
                islandCanSelect=true;
                break;
            case "lumberjack.jpg":
            case "thief.jpg":
                makeAppearTheColorHbox(hboxColorCharacter,false);
                break;
            case "storyteller.jpg":
                //scambio scuola entrata
                makeAppearTheColorHbox(hboxColorCharacter,true);
                makeAppearTheColorHbox(hboxColorCharacter1,true);
                break;
            default:
            break;
        }

    }

    /**
     * a box containing the students of all the colors among which the player can choose is set to visible
     * in the case of the storyteller card the box is used to select the students from the dining hall, so the box is
     * duplicated, since the player can choose more up to two students, also of the same color
     * @param box list of color
     * @param isStoryTeller boolean variable
     */
    public void makeAppearTheColorHbox(HBox box, boolean isStoryTeller){
        box.getChildren().clear();
        for(Color c: Color.values()){
            Image studentImg = new Image(getClass().getResourceAsStream("/graphics/board/" + c.getFileStudent()));
            ImageView studentImgview = new ImageView(studentImg);
            studentImgview.setFitHeight(15);
            studentImgview.setPreserveRatio(true);

            studentImgview.setUserData(c);
            studentImgview.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    if(colorSelected==0 && !isStoryTeller){
                        color=c;
                        ((Node)mouseEvent.getSource()).setEffect(new DropShadow(BlurType.GAUSSIAN, javafx.scene.paint.Color.DARKORANGE, 15, 0.7, 0, 0 ));
                        colorSelected++;
                    }else if(isStoryTeller && colorSelected<2){
                        if(colorSelected==0){
                            choosenStudent=new EnumMap<Color, Integer>(Color.class);
                            for(Color c:Color.values())
                                choosenStudent.put(c,0);
                        }
                        ImageView actualImageStudent=(ImageView) mouseEvent.getSource();
                        actualImageStudent.setEffect(new DropShadow(BlurType.GAUSSIAN, javafx.scene.paint.Color.DARKORANGE, 15, 0.7, 0, 0 ));
                        Color choosenColor=(Color)actualImageStudent.getUserData();
                        choosenStudent.put(choosenColor,choosenStudent.get(choosenColor)+1);
                        colorSelected++;
                    }

                }
            });
            box.getChildren().add(studentImgview);
        }
        box.setVisible(true);

    }

    /**
     * when clicking on the students on the card, select them and set the variable chosen students
     * @param event click on students on the card
     */
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

    /**
     * show the schools of the players, filling the grids with the students, the professors and the towers
     */
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

    /**
     * show the students on the cloud on screen, filling the grids
     */
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

    /***
     * Method used for send to the Server the wizard chosen
     * @param event
     */
    public void chooseWizard(Event event){
        Node node = (Node) event.getSource() ;
        String wizard = (String) node.getUserData();
        gui.send(new ChooseWizard(Wizards.valueOf(Wizards.class,wizard.replaceAll(" ", "").toUpperCase())));
    }

    /***
     * Show the available assistant card and let player choose one
     * @param msg The message contains the list of assistant cards available
     */
    public void selectAssistantCard(SelectAssistantCard msg){
        mySchool.setVisible(false);
        showStudentsAndTowers(false);
        wizardAndMoney.setVisible(false);
        character.setVisible(false);
        cardGrid1.setVisible(false);
        cardGrid2.setVisible(false);
        cardGrid3.setVisible(false);
        if(null!=gui.getVmodel().getCharacterCards())
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

    /***
     * Send the chosen assistant card to Server
     * @param event The assistant card event
     */
    public void chooseAssistant(Event event){
        Node node = (Node) event.getSource() ;
        String assistant = (String) node.getUserData();
        int value = 0;
        for(int i=0; i<gui.getVmodel().getClientPlayer().getAssistantCards().size();i++) {
            if (gui.getVmodel().getClientPlayer().getAssistantCards().get(i).getName().equalsIgnoreCase(assistant)) {
                value = gui.getVmodel().getClientPlayer().getAssistantCards().get(i).getValue();
                player1Assistant.setImage(new Image(getClass().getResourceAsStream(gui.getVmodel().getClientPlayer().getAssistantCards().get(i).getAsset())));
                break;
            }

        }
        gui.send(new PlayAssistantCard(value));
        changePhase();
    }

    /***
     * Send to server the cloud chosen by the player
     * @param event The event associate to the cloud
     */
    public void chooseCloud(MouseEvent event){
        if(currentPhase==GamePhase.CHOOSECLOUD){
            Node node=(Node) event.getSource();
            int cloudId=(int) node.getUserData();
            gui.send((new ChooseCloud(cloudId)));
            playedCharacterCard=false;
        }
    }

    public void setCurrentPhase(GamePhase currentPhase) {
        this.currentPhase = currentPhase;
    }


    /***
     * Make the selected student glow and set the value in Move Student phase
     * @param event The event associated to student
     */
    public void setSelectedStudent(MouseEvent event){
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

    /**
     * set an effect on mother nature's image to indicate that is selected to be moved
     * @param event click on mother nature
     */
    public void setSelectedMotherNature(MouseEvent event){
        if(currentPhase.equals(GamePhase.MOVEMOTHERNATURE)){
            Node node = (Node) event.getSource();
            node.setEffect(new DropShadow(0, javafx.scene.paint.Color.DARKORANGE));
            node.setEffect(new DropShadow(BlurType.GAUSSIAN, javafx.scene.paint.Color.DARKORANGE, 15, 0.7, 0, 0 ));
        }
    }

    /**
     * reposition of the islands after merge, and remove the ones that have been merged with others
     * @param msg message containing info about the islands merged
     */
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

            if(x>436 && x<844 && y>135 && y<372){
                if(x<572)
                    x=236;
                else if(x<=708){
                    if(y<=253)
                        y=0;
                    else y=372;
                }
                else x=844;
            }

            if(mergedIsland1<mergedIsland2){
                islandGridsList.remove(mergedIsland2);
                islandTowerGridsList.remove(mergedIsland2);
                islandGroupsList.get(mergedIsland2).getChildren().clear();
                islandGroupsList.remove(mergedIsland2);
                islandGroupsList.get(mergedIsland1).setLayoutX(x);
                islandGroupsList.get(mergedIsland1).setLayoutY(y);
            }else{
                islandGridsList.remove(mergedIsland1);
                islandTowerGridsList.remove(mergedIsland1);
                islandGroupsList.get(mergedIsland1).getChildren().clear();
                islandGroupsList.remove(mergedIsland1);
                islandGroupsList.get(mergedIsland2).setLayoutX(x);
                islandGroupsList.get(mergedIsland2).setLayoutY(y);
            }
            conqueredIsland= (conqueredIsland==islandGridsList.size()) ? conqueredIsland-1 : conqueredIsland;
            islandGridsList.remove(conqueredIsland);
            islandTowerGridsList.remove(conqueredIsland);
            islandGroupsList.get(conqueredIsland).getChildren().clear();
            islandGroupsList.remove(conqueredIsland);
        }else{
            double x=(x1+x2)/2;
            double y=(y1+y2)/2;
            if(x>436 && x<844 && y>135 && y<372){
                if(x<572)
                    x=236;
                else if(x<=708){
                    if(y<=253)
                        y=0;
                    else y=372;
                }
                else x=844;
            }
            if(mergedIsland1<conqueredIsland){
                islandGridsList.remove(conqueredIsland);
                islandTowerGridsList.remove(conqueredIsland);
                islandGroupsList.get(conqueredIsland).getChildren().clear();
                islandGroupsList.remove(conqueredIsland);
                islandGroupsList.get(mergedIsland1).setLayoutX(x);
                islandGroupsList.get(mergedIsland1).setLayoutY(y);
            }else{
                islandGridsList.remove(mergedIsland1);
                islandTowerGridsList.remove(mergedIsland1);
                islandGroupsList.get(mergedIsland1).getChildren().clear();
                islandGroupsList.remove(mergedIsland1);
                islandGroupsList.get(conqueredIsland).setLayoutX(x);
                islandGroupsList.get(conqueredIsland).setLayoutY(y);
            }
        }
    }

    /**
     * set a boolean variable to true when the player uses a character card in during a turn, so that he will not be
     * able to use another one in the same turn
     * @param playedCharacterCard
     */
    public void setPlayedCharacterCard(boolean playedCharacterCard) {
        this.playedCharacterCard = playedCharacterCard;
    }
}
