package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;

/**
 * Update message that is sent at every change of the game model
 * @author Giuseppe Bonanno, Federica Tommasini, Angelo Zagami
 */
public class UpdateMessage implements ServerToClientMessage{

     private final BoardChange change;

    public UpdateMessage(BoardChange change) {
        this.change = change;
    }

    public BoardChange getChange() {
        return change;
    }

    @Override
    public void handle(View view) throws IOException {
        view.update(this);
    }

}
