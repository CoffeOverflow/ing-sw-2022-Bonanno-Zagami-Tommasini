package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Client.VirtualModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends Application implements Runnable
{
    private ServerHandler serverHandler;
    private VirtualModel vmodel;
    private Stage stage;
    private Scene currentScene;

    public GUI(){
        this.vmodel = new VirtualModel();
    }

    @Override
    public void start(Stage stage) throws Exception {
        //setup();
        this.stage = stage;
        Font.loadFont(getClass().getResourceAsStream("/fonts/newborough.ttf"), 14);
        run();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void run() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene sc = new Scene(root);
        sc.getStylesheets().addAll(this.getClass().getResource("/css/mainmenu.css").toExternalForm());
        stage.setScene(sc);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.setTitle("Eriantys");
        stage.show();
    }
}