package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Model.AssistantCard;
import java.io.IOException;

/***
 * Message sent to the players to tell the assistant card played by other player
 * @author Angelo Zagami
 */
public class PlayerPlayAssistantCard implements ServerToClientMessage{
    private int playerID;
    private AssistantCard card;

    public PlayerPlayAssistantCard(int playerID, AssistantCard card){
        this.playerID = playerID;
        this.card = card;
    }

    @Override
    public void handle(View view) throws IOException {
        view.playerPlayAssistantCard(playerID, card);
    }
}
