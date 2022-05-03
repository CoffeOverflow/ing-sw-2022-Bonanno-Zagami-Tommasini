package it.polimi.ingsw.Server.ServerToClient;

public class OtherPlayerWins implements ServerToClientMessage{
    private String msg;
    private String winnerNickname;

    public OtherPlayerWins(String winnerNickname) {
        this.winnerNickname = winnerNickname;
        msg=winnerNickname+"won!";
    }

    public String getWinnerNickname() {
        return winnerNickname;
    }

    public String getMsg() {
        return msg;
    }
}
