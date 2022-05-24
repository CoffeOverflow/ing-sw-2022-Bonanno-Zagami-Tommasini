package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Model.Wizards;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;

public class ChooseWizard implements ClientToServerMessage{
    private Wizards wizard;

    public ChooseWizard(Wizards wizardName){
        wizard = wizardName;
    }

    public Wizards getWizard() {
        return wizard;
    }

    public void handleMessage(GameHandler game, ClientHandler player){
        game.playerChooseWizard(wizard, player);
    }

}
