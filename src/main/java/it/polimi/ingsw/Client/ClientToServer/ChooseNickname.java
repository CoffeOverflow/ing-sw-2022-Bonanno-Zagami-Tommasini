package it.polimi.ingsw.Client.ClientToServer;

public class ChooseNickname implements SetUpMessage{
    private String nickname;

    public ChooseNickname(String nickname){
        this.nickname=nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
