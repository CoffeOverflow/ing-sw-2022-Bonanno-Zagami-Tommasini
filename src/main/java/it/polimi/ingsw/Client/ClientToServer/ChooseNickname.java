package it.polimi.ingsw.Client.ClientToServer;

/**
 * ChooseNickname class
 * implementation of a setUp message from client to server to indicate the nickname chosen by the player
 * @author Federica Tommasini
 */
public class ChooseNickname implements SetUpMessage{
    private final String nickname;

    public ChooseNickname(String nickname){
        this.nickname=nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
