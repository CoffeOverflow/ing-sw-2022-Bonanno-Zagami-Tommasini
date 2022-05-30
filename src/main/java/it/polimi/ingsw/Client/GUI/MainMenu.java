package it.polimi.ingsw.Client.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;


public class MainMenu extends Application
{
    // When using IntelliJ, don't run the application from here, use the main
    // method in JavaFXMain
    public static void main(String[] args)
    {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage)
    {
        Font.loadFont(getClass().getResourceAsStream("/fonts/newborough.ttf"), 14);
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene sc = new Scene(root);
        sc.getStylesheets().addAll(this.getClass().getResource("/css/mainmenu.css").toExternalForm());
        primaryStage.setScene(sc);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Eriantys");
        primaryStage.show();
    }
}
