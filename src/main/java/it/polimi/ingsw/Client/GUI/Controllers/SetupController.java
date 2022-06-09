package it.polimi.ingsw.Client.GUI.Controllers;

import it.polimi.ingsw.Client.ClientToServer.ChooseNickname;
import it.polimi.ingsw.Client.ClientToServer.SelectMatch;
import it.polimi.ingsw.Client.ClientToServer.SelectModeAndPlayers;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Server.ServerToClient.ChooseMatch;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Scanner;
import java.util.regex.Pattern;

import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.WHITE;

public class SetupController implements GUIController{
    private GUI gui;
    @FXML public AnchorPane mainPane;
    public VBox requestNickname;
    public VBox chooseMatch;
    public VBox listOfMatch;
    public VBox newgameform;
    public VBox wait;
    public TextField nickname;
    public Button send;
    public RadioButton twoPlayers;
    public RadioButton threePlayers;
    public RadioButton expertmode;
    public RadioButton normalmode;
    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(message.replaceAll("\u001B\\[[\\d;]*[^\\d;]",""));
        alert.showAndWait();
    }

    public void initialize() {
        requestNickname.setVisible(false);
        chooseMatch.setVisible(false);
        newgameform.setVisible(false);
        wait.setVisible(false);
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
                    //gameName.setFont(new Font("Arial", 18));
                    gameName.setStyle("-fx-font-size: 18");
                    Button join = new Button("Join");
                    join.prefHeight(30);
                    join.prefWidth(100);
                    join.setUserData(line.split("\\.")[0]);
                    join.setOnAction(new EventHandler() {
                        @Override
                        public void handle(Event event) {
                            joinGame(event);
                        }
                    });
                    game.getChildren().add(gameName);
                    game.setSpacing(20);
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
                    //gameName.setFont(new Font("Arial", 26));
                    gameName.setStyle("-fx-font-size: 26");
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
    public void newgameButtonClicked(){
        chooseMatch.setVisible(false);
        newgameform.setVisible(true);
        gui.send(new SelectMatch(0));
    }
    public void createnewgameButtonClicked(){
        if((twoPlayers.isSelected() || threePlayers.isSelected()) && (expertmode.isSelected() || normalmode.isSelected())){
            newgameform.setVisible(false);
            gui.send(new SelectModeAndPlayers(twoPlayers.isSelected() ? 2:3, expertmode.isSelected()));
            System.out.println(twoPlayers.isSelected() ? 2:3);
        }
        else{
            gui.showError("Some settings are missing, please select all the option!");
        }

    }

    public void joinGame(Event event){
        chooseMatch.setVisible(false);
        Node node = (Node) event.getSource() ;
        int match = Integer.parseInt((String) node.getUserData());
        gui.send(new SelectMatch(match));

    }

    public void waitForOtherPlayers(){
        chooseMatch.setVisible(false);
        wait.setVisible(true);
    }
}
