package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.Wizards;

public class ChooseWizard implements ClientToServerMessage{
    private Wizards wizard;

    public ChooseWizard(String wizardName){
        wizard=Wizards.valueOf(Wizards.class,wizardName);
    }

    public Wizards getWizard() {
        return wizard;
    }

    public void handleMessage(GameController controller){
        int playerId=controller.getModel().getCurrentPlayer();
        controller.getModel().getPlayerByID(playerId).setWizard(wizard);
    }

}
