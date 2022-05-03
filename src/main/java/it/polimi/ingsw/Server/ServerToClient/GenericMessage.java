package it.polimi.ingsw.Server.ServerToClient;

public class GenericMessage implements ServerToClientMessage{
    private String message;

    public GenericMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
