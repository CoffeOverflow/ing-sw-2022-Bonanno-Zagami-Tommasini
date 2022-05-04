package it.polimi.ingsw.Client;

import java.io.IOException;

public interface View {
    void requestNickname() throws IOException;

    void showError(String error);

    void actionValid(String message);

    void chooseMatch(String message) throws IOException;

    void showMessage(String message);

    void requestSetup() throws IOException;


}
