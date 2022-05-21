package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

public class ChooseOption implements ServerToClientMessage{

    private String msg;
    private OptionType type;

    public ChooseOption(OptionType type){
        this.type=type;
        switch (type){
            case MOVESTUDENTS:
                msg="Choose an option: \n1.Move three students \n2.Play a character card \n";
                break;
            case MOVENATURE:
                msg="Choose an option: \n1.Move mother nature \n2.Play a character card \n";
                break;

        }
    }

    public String getMsg() {
        return msg;
    }

    public OptionType getType() {
        return type;
    }

    public void handle(View view){
        view.chooseOption(this);
    }


}
