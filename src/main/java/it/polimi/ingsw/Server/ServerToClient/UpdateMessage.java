package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.Color;

public class UpdateMessage implements ServerToClientMessage{

     private BoardChange change;

    public UpdateMessage(BoardChange change) {
        this.change = change;
    }

    public BoardChange getChange() {
        return change;
    }
}
