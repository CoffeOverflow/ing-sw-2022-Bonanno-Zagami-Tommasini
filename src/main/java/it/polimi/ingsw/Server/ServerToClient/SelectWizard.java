package it.polimi.ingsw.Server.ServerToClient;

import java.io.Serializable;
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
}
