package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;
import java.util.List;

public class SelectWizard implements ServerToClientMessage {

    private static String msg="Select a wizard among the ones available";

    private List<String> availableWizards;

    public SelectWizard(List<String> availableWizards){
        this.availableWizards=availableWizards;
    }

    public static String getMsg() {
        return msg;
    }

    public List<String> getAvailableWizards() {
        return availableWizards;
    }

    @Override
    public void handle(View view) throws IOException {

    }
}
