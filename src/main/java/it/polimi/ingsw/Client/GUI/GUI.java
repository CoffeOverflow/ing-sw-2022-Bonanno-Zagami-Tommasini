package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.GUI.Controllers.GUIController;
import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.VirtualModel;
import it.polimi.ingsw.Server.ServerToClient.ServerHeartbeat;
import it.polimi.ingsw.Server.ServerToClient.ServerToClientMessage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class GUI extends Application implements Runnable
{
    private ServerHandler serverHandler;
    private VirtualModel vmodel;
    private Stage stage;
    private Scene currentScene;
    private HashMap<String, Scene> nameToScene = new HashMap<>();
    private HashMap<String, GUIController> nameToController = new HashMap<>();

    public GUI(){
        this.vmodel = new VirtualModel();
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
                nameToController.put(scene.getName(), loader.getController());
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
                    //fromServer.handle(this);
                    return;

            } catch (IOException | ClassNotFoundException | RuntimeException e) {
                //showError("\nConnection error, maybe one player left the match. The app will now close!");
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

}