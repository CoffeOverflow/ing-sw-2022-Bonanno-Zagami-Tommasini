package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;
import java.io.IOException;

/**
 * implementation of a message from server to client to inform the client of an error
 * @author Angelo Zagami, Giuseppe Bonanno
 */
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

    @Override
    public void handle(View view) throws IOException {
        view.showError(message);
        if(error==ErrorsType.NOTENOUGHMONEY){
            view.setUseCharcaterCard();
        }
    }
}
