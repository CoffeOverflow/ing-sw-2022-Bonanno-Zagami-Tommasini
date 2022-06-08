package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.GUI.Controllers.GUIController;
import it.polimi.ingsw.Client.GUI.Controllers.GameController;
import it.polimi.ingsw.Client.GUI.Controllers.SetupController;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Client.VirtualModel;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.ServerToClient.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class GUI extends Application implements Runnable, View
{
    private ServerHandler serverHandler;
    private VirtualModel vmodel;
    private Stage stage;
    private Scene currentScene;
    private HashMap<String, Scene> nameToScene = new HashMap<>();
    private HashMap<Scene, GUIController> sceneToController = new HashMap<>();

    public GUI(){
        this.vmodel = new VirtualModel();
    }

    public void send(Object message){
        serverHandler.send(message);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        setup();
        this.stage = stage;
        displayScene();
    }

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

    @Override
    public void run() {
        while(true){
            ServerToClientMessage fromServer = null;
            try {
                fromServer = serverHandler.read();
                if(!(fromServer instanceof ServerHeartbeat))
                    fromServer.handle(this);

            } catch (IOException | ClassNotFoundException | RuntimeException e) {
                e.printStackTrace();
                showError("\nConnection error, maybe one player left the match. The app will now close!");
                System.exit(-1);
            }
        }
    }

    public void displayScene() {
        stage.setScene(currentScene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.setTitle("Eriantys");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/icons/mother_nature.png")));
        stage.show();
    }

    public void changeScene(String scene){
        this.currentScene = nameToScene.get(scene);
        displayScene();
    }

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

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
        vmodel.setTakeProfessorWhenTie(false);
    }

    @Override
    public void youWin() {
        serverHandler.close();
    }

    @Override
    public void otherPlayerWins(OtherPlayerWins msg){
        this.showMessage(msg.getMsg());
    }

    @Override
    public void selectAssistantCard(SelectAssistantCard msg) {

    }

    @Override
    public void update(UpdateMessage msg) {
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
    public void showBoard() {

    }

    @Override
    public void showIsland() {

    }

    @Override
    public void showSchool(Player p,String colorTower) {

    }

    @Override
    public void showClouds() {

    }

    @Override
    public void chooseOption(ChooseOption msg) {

    }

    @Override
    public void showCharacterCard() {

    }
}