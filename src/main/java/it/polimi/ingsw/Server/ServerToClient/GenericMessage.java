package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

import java.io.IOException;

public class GenericMessage implements ServerToClientMessage{
    private String message;

    public GenericMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void handle(View view) throws IOException {
        view.printMessage(message);
    }
}
