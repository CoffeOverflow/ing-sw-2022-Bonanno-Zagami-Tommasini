package it.polimi.ingsw.Client.ClientToServer;

public class ChooseNickname implements ClientToServerMessage{
    private String nickname;

    public ChooseNickname(String nickname){
        this.nickname=nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
