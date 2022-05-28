package it.polimi.ingsw.Client.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class JavaFXMainMenu extends Application
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
        primaryStage.show();
    }
}
