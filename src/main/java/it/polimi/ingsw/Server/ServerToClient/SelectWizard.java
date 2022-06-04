package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Model.Wizards;

import java.io.IOException;
import java.util.List;

public class SelectWizard implements ServerToClientMessage {

    private String msg="Select a wizard among the ones available:\n";

    private List<Wizards> availableWizards;

    public SelectWizard(List<Wizards> availableWizards){
        this.availableWizards=availableWizards;
    }

    public String getMsg() {
        return msg;
    }

    public List<Wizards> getAvailableWizards() {
        return availableWizards;
    }

    @Override
    public void handle(View view) throws IOException {
        view.chooseWizard(this);
    }
}
