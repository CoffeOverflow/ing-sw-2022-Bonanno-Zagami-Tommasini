package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Server.ClientHandler;

public class ChooseNickname implements SetUpMessage{
    private String nickname;

    public ChooseNickname(String nickname){
        this.nickname=nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
