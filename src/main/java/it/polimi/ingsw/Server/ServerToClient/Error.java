package it.polimi.ingsw.Server.ServerToClient;

public class Error implements ServerToClientMessage{
    private ErrorsType error;
    private String message;

    public Error(ErrorsType error, String message){
        this.error = error;
        this.message = message;
    }
    public Error(ErrorsType error){
        this.error = error;
        this.message = null;
    }

    public String getMessage() {
        return message;
    }
}
