package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;

public class UpdateMessage implements ServerToClientMessage{

     private BoardChange change;

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
