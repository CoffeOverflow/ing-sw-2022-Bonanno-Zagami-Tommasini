package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.GUI.Controllers.EndGameController;
import it.polimi.ingsw.Client.GUI.Controllers.GUIController;
import it.polimi.ingsw.Client.GUI.Controllers.GameController;
import it.polimi.ingsw.Client.GUI.Controllers.SetupController;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Client.VirtualModel;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Server.ServerToClient.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

import static it.polimi.ingsw.Constants.*;

/***
 * GUI main class
 * @author Giuseppe Bonanno, Federica Tommasini, Angelo Zagami
 * @see View
 */
public class GUI extends Application implements Runnable, View
{
    private ServerHandler serverHandler;
    private VirtualModel vmodel;
    private Stage stage;
    private Scene currentScene;
    private HashMap<String, Scene> nameToScene = new HashMap<>();
    private HashMap<Scene, GUIController> sceneToController = new HashMap<>();

    private boolean firstUpdate=true;

    /***
     * Class constructor
     */
    public GUI(){
        this.vmodel = new VirtualModel();
    }

    /***
     * Send a message to server
     * @param message Message to send
     */
    public void send(Object message){
        serverHandler.send(message);
    }

    /***
     * Main of the class
     * @param args Main args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /***
     * This method load the scenes set the stage and display the scene
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        setup();
        this.stage = stage;
        this.stage.setResizable(false);
        displayScene();
    }

    /***
     * Setup of fxml files of the application. This method load the files of each scenes
     */
    public void setup(){
        Font.loadFont(getClass().getResourceAsStream("/fonts/newborough.ttf"), 14);
        try {
            for (Scenes scene : Scenes.values()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + scene.getFile()));
                nameToScene.put(scene.getName(), new Scene(loader.load()));
                GUIController controller = loader.getController();
                controller.setGUI(this);
                sceneToController.put(nameToScene.get(scene.getName()), loader.getController());
            }
            currentScene = nameToScene.get("MENU");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /***
     * Main loop of the GUI, it recive messages from server and execute the handle
     */
    @Override
    public void run() {
        while(!endGame){
            ServerToClientMessage fromServer = null;
            try {
                fromServer = serverHandler.read();
                if(!(fromServer instanceof ServerHeartbeat))
                    fromServer.handle(this);

            } catch (IOException | ClassNotFoundException | RuntimeException e) {
                if(!endGame){
                    showError("Connection error, maybe one player left the match. The app will now close!");
                    endGame = true;
                }
            }
        }
        serverHandler.close();
    }

    /***
     * Display scene on screen
     */
    public void displayScene() {
        stage.setScene(currentScene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.setTitle("Eriantys");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/icons/mother_nature.png")));
        stage.show();
    }

    /***
     * Change displayed scene
     * @param scene The scene to show
     */
    public void changeScene(String scene){
        this.currentScene = nameToScene.get(scene);
        stage.setScene(currentScene);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        stage.show();
    }

    /***
     * Set the server handler
     * @param serverHandler
     */
    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    /***
     * Set wait for other players scene
     */
    public void waitForOtherPlayers(){
        if(currentScene.equals(nameToScene.get("SETUP"))){
            SetupController controller = (SetupController) sceneToController.get(currentScene);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.waitForOtherPlayers();
                }
            });

        }
    }

    /***
     * Change scene to GAME
     */
    public void startGame(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                changeScene("GAME");
            }
        });
    }


    @Override
    public void requestNickname() throws IOException {
        if(currentScene.equals(nameToScene.get("SETUP"))){
            SetupController controller = (SetupController) sceneToController.get(currentScene);
            controller.showNicknameField();
        }
    }

    /***
     * Show error on the screen with a popup
     * @param error The error message
     */
    @Override
    public void showError(String error) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                sceneToController.get(currentScene).showError(error);
            }
        });

    }

    @Override
    public void setUseCharcaterCard(){
        this.vmodel.setUseCharacterCard(false);
    }

    @Override
    public void actionValid(String message) {
        //CAMBIO STATO IN BASE AL GAME?
        if(currentScene.equals(nameToScene.get("GAME"))){
            GameController controller = (GameController) sceneToController.get(currentScene);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.changePhase();
                }
            });

        }
    }

    @Override
    public void chooseMatch(String message) throws IOException {
        if(currentScene.equals(nameToScene.get("SETUP"))){
            SetupController controller = (SetupController) sceneToController.get(currentScene);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.showChooseMatch(message);
                }
            });

        }
    }

    @Override
    public void showMessage(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                sceneToController.get(currentScene).showMessage(message);
            }
        });
    }

    @Override
    public void requestSetup() throws IOException {

    }

    @Override
    public void matchCreated(MatchCreated msg) {
        this.vmodel.setIslandsAndMotherNature(msg);
    }

    @Override
    public void playersInfo(PlayersInfo msg) {
        this.vmodel.setPlayersInfo(msg);
    }

    @Override
    public void setUpCharacterCard(SetUpCharacterCard msg){
        this.vmodel.setCharacterCards(msg);
    }

    @Override
    public void setUpSchoolStudent(SetUpSchoolStudent msg){
        this.vmodel.setSchoolStudents(msg);
    }

    @Override
    public void isTurnOfPlayer(String msg){
        this.showMessage(msg);
        GameController controller = (GameController) sceneToController.get(currentScene);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.changePhase(msg.contains("it's your turn")? GamePhase.MOVESTUDENT : GamePhase.ANOTHERPLAYERTURN);
            }
        });

        vmodel.setTakeProfessorWhenTie(false);
    }

    @Override
    public void youWin() {
        System.out.println("ENDGAME");
        endGame = true;
        serverHandler.close();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                changeScene("ENDGAME");
                EndGameController controller = (EndGameController) sceneToController.get(currentScene);
                controller.win();
            }
        });
    }

    @Override
    public void otherPlayerWins(OtherPlayerWins msg){
        System.out.println("ENDGAME");
        endGame = true;
        serverHandler.close();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                changeScene("ENDGAME");
                EndGameController controller = (EndGameController) sceneToController.get(currentScene);
                controller.lose(msg.getMsg());
            }
        });
    }

    @Override
    public void selectAssistantCard(SelectAssistantCard msg) {
        GameController controller = (GameController) sceneToController.get(currentScene);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.selectAssistantCard(msg);
            }
        });
    }

    @Override
    public void update(UpdateMessage msg) {

        if(vmodel.getClouds().isEmpty() || msg.getChange().getChange()==Change.MERGE){
            vmodel.update(msg);
            GameController controller = (GameController) sceneToController.get(currentScene);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(firstUpdate) {
                        /*
                         * first update: set up the grids to visualize students in the board
                         */
                        controller.setGrids();
                        firstUpdate=false;
                    }
                    if(msg.getChange().getChange()==Change.MERGE) {
                        controller.handleMerge(msg);
                    }else if(msg.getChange().getChange()==Change.PLAYCLOWN
                            || msg.getChange().getChange()==Change.PLAYHERBALIST
                            || msg.getChange().getChange()==Change.PLAYINNKEEPER
                            || msg.getChange().getChange()==Change.PLAYPRINCESS
                            || msg.getChange().getChange()==Change.PLAYSTORYTELLER
                            || msg.getChange().getChange()==Change.PLAYTHIEF
                            || msg.getChange().getChange()==Change.PLAYMERCHANT
                            || msg.getChange().getChange()==Change.DEFAULT)
                        /*
                         * the update regards a character card, so the player in that turn will not be able to play another card
                         */
                        controller.setPlayedCharacterCard(true);
                }
            });

            this.showBoard();
            return;
        }
        vmodel.update(msg);
        this.showBoard();
    }

    @Override
    public void chooseWizard(SelectWizard message) throws IOException {
        if(currentScene.equals(nameToScene.get("GAME"))){
            GameController controller = (GameController) sceneToController.get(currentScene);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.selectWizard(message);
                }
            });

        }
    }

    @Override
    public void showBoard(){
        if(currentScene.equals(nameToScene.get("GAME"))){
            GameController controller = (GameController) sceneToController.get(currentScene);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.showBoard();
                }
            });

        }

    }

    @Override
    public void chooseOption(ChooseOption msg) {
        GameController controller = (GameController) sceneToController.get(currentScene);
        if(msg.getType()==OptionType.MOVESTUDENTS)
            controller.setCurrentPhase(GamePhase.MOVESTUDENT);
        else if(msg.getType()==OptionType.MOVENATURE){
            vmodel.resetNumOfInstance();
            controller.setCurrentPhase(GamePhase.MOVEMOTHERNATURE);
        }else if(msg.getType()== OptionType.CHOOSECLOUD)
            controller.setCurrentPhase(GamePhase.CHOOSECLOUD);

    }

    @Override
    public VirtualModel getVmodel() {
        return vmodel;
    }

    @Override
    public void playerPlayAssistantCard(int playerID, AssistantCard card) {
        if(currentScene.equals(nameToScene.get("GAME"))){
            GameController controller = (GameController) sceneToController.get(currentScene);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.playerPlayAssistantCard(playerID, card);
                }
            });

        }
    }
}