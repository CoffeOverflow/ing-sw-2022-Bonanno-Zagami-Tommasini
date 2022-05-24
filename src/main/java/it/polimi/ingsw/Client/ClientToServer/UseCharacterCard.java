package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;

import java.util.EnumMap;

public class UseCharacterCard implements ClientToServerMessage{

    private String asset;
    private Color color;
    private Integer posIsland;
    private EnumMap<Color,Integer> choosenStudents;
    private EnumMap<Color,Integer> entranceStudents;

    public UseCharacterCard(String asset,Integer posIsland, EnumMap<Color,Integer> choosenStudents,EnumMap<Color,Integer> entranceStudents,Color color){
        this.asset=asset;
        this.choosenStudents=choosenStudents;
        this.entranceStudents=entranceStudents;
        this.posIsland=posIsland;
        this.color=color;
    }

    public void handleMessage(GameHandler game, ClientHandler player){
        Action action=new Action();
        if(choosenStudents!=null)
            action.setChosenStudents(choosenStudents);
        if(color!=null)
            action.setChosenColor(color);
        if(entranceStudents!=null)
            action.setEntranceStudents(entranceStudents);
        if(asset!=null)
            action.setAsset(asset);
        action.setPosIsland(posIsland);
        game.getController().doAction(action);

    }
}
