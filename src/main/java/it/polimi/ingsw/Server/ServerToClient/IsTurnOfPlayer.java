package it.polimi.ingsw.Server.ServerToClient;

public class IsTurnOfPlayer implements ServerToClientMessage{

    private String msg;

    public IsTurnOfPlayer(String playerNickname){
        msg="it's the turn of "+playerNickname;
    }

    public String getMsg() {
        return msg;
    }
}
