package it.polimi.ingsw.Client;

import java.io.IOException;

public interface View {

    void requestNickname() throws IOException;

    void showError(String error);

    void actionValid(String message);

    void chooseMatch(String message) throws IOException;

    void showMessage(String message);

    void requestSetup() throws IOException;

    /*
    void actionNonValid(String message);

    void update(ServerToClientMessage msg);

    void matchCreated(ServerToClientMessage msg);

    void isTurnOfPlayer(ServerToClientMessage msg);

    void otherPlayerWins(ServerToClientMessage msg);

    void playersInfo(ServerToClientMessage msg);

    void selectAssistantCard(ServerToClientMessage msg);

    void setUpCharacterCard(ServerToClientMessage msg);

    void setUpSchoolStudent(ServerToClientMessage msg);

    void youWin(ServerToClientMessage msg);



     */
}
