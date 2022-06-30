package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import it.polimi.ingsw.Model.AssistantCard;
import java.io.IOException;

/**
 * implementation of a message from server to client to inform other players of an assistant card played by a player
 * @author Angelo Zagami
 */
public class PlayerPlayAssistantCard implements ServerToClientMessage{
    private final int playerID;
    private final AssistantCard card;

    public PlayerPlayAssistantCard(int playerID, AssistantCard card){
        this.playerID = playerID;
        this.card = card;
    }

    @Override
    public void handle(View view) throws IOException {
        view.playerPlayAssistantCard(playerID, card);
    }
}
