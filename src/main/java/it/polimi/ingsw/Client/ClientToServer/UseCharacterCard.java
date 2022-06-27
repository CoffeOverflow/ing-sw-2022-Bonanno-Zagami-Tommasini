package it.polimi.ingsw.Client.ClientToServer;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.State.DecideFirstPlayerState;
import it.polimi.ingsw.Controller.State.MoveStudentsState;
import it.polimi.ingsw.Controller.State.PlayCharacterCardState;
import it.polimi.ingsw.Controller.State.TakeStudentsState;
import it.polimi.ingsw.Exceptions.MoneyException;
import it.polimi.ingsw.Model.CharacterCard;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.ServerToClient.*;
import it.polimi.ingsw.Server.ServerToClient.Error;

import java.util.EnumMap;

import static it.polimi.ingsw.Constants.ANSI_RED;
import static it.polimi.ingsw.Constants.ANSI_RESET;

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
        if(posIsland!=null)
            action.setPosIsland(posIsland);

       try{
           if(!(game.getController().getState() instanceof PlayCharacterCardState))
                game.getController().setStateToReturn(game.getController().getState());
           game.getController().setState(new PlayCharacterCardState());
           game.getController().doAction(action);
           EnumMap<Color,Integer> cardStudents=new EnumMap<Color, Integer>(Color.class);
           for(CharacterCard c:game.getController().getModel().getCharacterCards()){
               if(c.getAsset().equals(asset) && null!=c.getStudents() && c.getStudents().isPresent())
                   cardStudents=c.getStudents().get().clone();
           }
           game.checkConquest();
           BoardChange change=new BoardChange(asset,posIsland,color,cardStudents,choosenStudents,entranceStudents,player.getPlayerID());
           String[] nameCard=asset.split("\\.");
           game.sendAllExcept(new GenericMessage(ANSI_RED+game.getController().getModel().getPlayerByID(player.getPlayerID()).getNickname()+" play the card "+nameCard[0]+ANSI_RESET),player);
           try {
               Thread.sleep(500);
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }
           game.sendAll(new UpdateMessage(change));
           try {
               Thread.sleep(500);
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }
       }catch (IllegalStateException e){

           if(e.getMessage().equals("Not enough money")) {
               game.sendTo(new Error(ErrorsType.NOTENOUGHMONEY), player);
               game.sendTo(new GenericMessage(ANSI_RED + "you don't have enough money to play the card!" + ANSI_RESET), player);
           }else if(e.getMessage().equals("Unexpected number of chosen students")){
               game.sendTo(new Error(ErrorsType.CHOSENOTVALID),player);
               game.sendTo(new GenericMessage(ANSI_RED + "you selected an incorrect number of students to play the card!" + ANSI_RESET), player);
           }
       }

        game.getController().setState(game.getController().getStateToReturn());
        if(game.getController().getState() instanceof MoveStudentsState || game.getController().getState() instanceof DecideFirstPlayerState || game.getController().getState() instanceof TakeStudentsState)
            game.sendTo(new ChooseOption(OptionType.MOVESTUDENTS,game.isExpertMode()), game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));
        else
            game.sendTo(new ChooseOption(OptionType.MOVENATURE,game.isExpertMode()), game.getClientByPlayerID(game.getController().getModel().getCurrentPlayer()));

    }
}
