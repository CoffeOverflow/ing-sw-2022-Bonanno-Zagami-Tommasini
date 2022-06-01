package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.ClientToServer.ChooseNickname;
import it.polimi.ingsw.Client.GUI.GUI;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Scanner;

import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.WHITE;

public class SetupController implements GUIController{
    private GUI gui;
    public VBox requestNickname;
    public VBox chooseMatch;
    public VBox listOfMatch;
    public TextField nickname;
    public Button send;
    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        //alert.setContentText("The entered IP/port doesn't match any active server or the server is not running. Please check errors and try again!");
        alert.showAndWait();
    }

    public void initialize() {
        requestNickname.setVisible(false);
        chooseMatch.setVisible(false);
    }

    public void showNicknameField(){
        requestNickname.setVisible(true);
    }
    public void showChooseMatch(String games){
        requestNickname.setVisible(false);
        chooseMatch.setVisible(true);
        Scanner scanner = new Scanner(games);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try{
                if(!line.contains("No match available!")){
                    HBox game = new HBox();
                    game.setAlignment(Pos.CENTER);
                    //Text gameName = new Text(line.replaceAll("\u001B\\[[\\d;]*[^\\d;]",""));
                    Label gameName = new Label(line.replaceAll("\u001B\\[[\\d;]*[^\\d;]",""));
                    gameName.prefHeight(333);
                    gameName.prefWidth(21);
                    gameName.setTextFill(WHITE);
                    Button join = new Button("Join");
                    join.prefHeight(30);
                    join.prefWidth(100);
                    game.getChildren().add(gameName);
                    game.setSpacing(100);
                    game.getChildren().add(join);
                    listOfMatch.getChildren().add(game);
                }
                else{
                    HBox game = new HBox();
                    game.setAlignment(Pos.CENTER);
                    Label gameName = new Label(line.replaceAll("\u001B\\[[\\d;]*[^\\d;]",""));
                    gameName.prefHeight(333);
                    gameName.prefWidth(21);
                    gameName.setTextFill(RED);
                    game.getChildren().add(gameName);
                    listOfMatch.getChildren().add(game);
                }

            }
            catch (RuntimeException e){
                e.printStackTrace();
            }

        }
        scanner.close();
    }

    public void sendButtonClicked(){
        gui.send(new ChooseNickname(nickname.getText()));
    }
}
