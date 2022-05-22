package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Client.View;

public class ChooseOption implements ServerToClientMessage{

    private boolean    expertMode;
    private String     msg;
    private OptionType type;

    public ChooseOption(OptionType type,boolean expertMode){
        this.type=type;
        this.expertMode=expertMode;
        switch (type){
            case MOVESTUDENTS:
                if(expertMode)
                    msg="Choose an option: \n1.Move three students \n2.Play a character card \n";
                else
                    msg="Move three students\n";
                break;
            case MOVENATURE:
                    if(expertMode)
                        msg="Choose an option: \n1.Move mother nature \n2.Play a character card \n";
                    else
                        msg="Move mother nature \n";
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

    public boolean isExpertMode() {return expertMode;}

}
