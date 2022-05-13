package it.polimi.ingsw.Client;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Server.ServerToClient.MatchCreated;
import it.polimi.ingsw.Server.ServerToClient.PlayersInfo;
import it.polimi.ingsw.Server.ServerToClient.SetUpCharacterCard;
import it.polimi.ingsw.Server.ServerToClient.SetUpSchoolStudent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VirtualModel {



    private List<Island> islands =new ArrayList<>();

    private int motherNaturePosition;

    private List<CharacterCard> characterCards=new ArrayList<>();

    private List<Player> players=new ArrayList<>();

    public void setIslandsAndMotherNature(MatchCreated msg){
        HashMap<Integer, Color> mapStudentIsland= msg.getMapStudentIsland();
        for(Integer i: mapStudentIsland.keySet()){
            islands.add(new Island());
            islands.get(i).addStudents(mapStudentIsland.get(i),1);
        }
        motherNaturePosition=msg.getMotherNaturePosition();
    }

    public void setPlayersInfo(PlayersInfo msg){
        int j=0;
        for(Integer i: msg.getMapIDNickname().keySet()){
            players.add(new Player(i,msg.getMapIDNickname().get(i), msg.isExpertMode(),
                    msg.getMapTowerToPlayer().get(i),msg.getNumberOfTowers()));
            players.get(j).setWizard(Wizards.valueOf(msg.getMapPlayerWizard().get(i).toUpperCase()));
            j++;
        }
    }

    public void setSchoolStudents(SetUpSchoolStudent msg){
        for(Player p: players){
            if(p.getPlayerID()==msg.getPlayerID())
                p.setEntryStudents(msg.getEntranceStudents());
        }
    }

    public void setCharacterCards(SetUpCharacterCard msg){
        int numberOfCard;
        for(int i=0; i<msg.getCharacterCards().length;i++){
            CharacterCard card=null;
            if(msg.getCharacterCards()[i].equals("innkeeper.jpg")
                    || msg.getCharacterCards()[i].equals("clown.jpg")
                    || msg.getCharacterCards()[i].equals("princess.jpg")){
                switch(i){
                    case 0:
                        card=new CharacterCard(msg.getCharacterCards()[i],msg.getFirstCardStudents());
                        break;
                    case 1:
                        card=new CharacterCard(msg.getCharacterCards()[i],msg.getSecondCardStudents());
                        break;
                    case 2:
                        card=new CharacterCard(msg.getCharacterCards()[i],msg.getThirdCardStudents());
                        break;
                }
            }
            else{
                card=new CharacterCard(msg.getCharacterCards()[i]);
            }
            characterCards.add(card);
        }

    }

    public List<Island> getIslands() {

        return islands;
    }

    public int getMotherNaturePosition() {

        return motherNaturePosition;
    }

    public List<CharacterCard> getCharacterCards() {

        return characterCards;
    }

    public List<Player> getPlayers() {

        return players;
    }

    public void moveMotherNature(int stpes){
        this.motherNaturePosition=(motherNaturePosition+stpes)%12;
    }
}
