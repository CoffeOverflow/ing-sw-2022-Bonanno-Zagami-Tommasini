package it.polimi.ingsw.Client.ClientToServer;

public class ChooseWizard implements ClientToServerMessage{
    private String wizard;

    public ChooseWizard(String wizardName){
        wizard=wizardName;
    }

    public String getWizard() {
        return wizard;
    }
}
