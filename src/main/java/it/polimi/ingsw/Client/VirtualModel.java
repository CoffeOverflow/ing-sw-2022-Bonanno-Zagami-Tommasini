package it.polimi.ingsw.Client;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Server.ServerToClient.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

public class VirtualModel {

    private List<Island> islands =new ArrayList<>();

    private int motherNaturePosition;

    private List<CharacterCard> characterCards=new ArrayList<>();

    private List<Player> players=new ArrayList<>();

    private Player clientPlayer;

    private List<Cloud> clouds=new ArrayList<>();

    public List<Cloud> getClouds() {
        return clouds;
    }

    public void setIslandsAndMotherNature(MatchCreated msg)  {
        HashMap<Integer, Color> mapStudentIsland = msg.getMapStudentIsland();
        for(int i=0; i<12; i++) {
            Island isl=new Island();
            if(mapStudentIsland.get(i)!=null)
                isl.addStudents(mapStudentIsland.get(i), 1);
            islands.add(isl);
        }

        motherNaturePosition=msg.getMotherNaturePosition();
    }

    public void setPlayersInfo(PlayersInfo msg){
        int j=0;
        for(Integer i: msg.getMapIDNickname().keySet()){
            players.add(new Player(i,msg.getMapIDNickname().get(i), msg.isExpertMode(),
                    msg.getMapTowerToPlayer().get(i),msg.getNumberOfTowers()));
            players.get(j).setWizard(msg.getMapPlayerWizard().get(i));
            if(players.get(j).getPlayerID()==msg.getYourPlayerID())
                clientPlayer=players.get(j);
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

    public void mergeIslands(int islandPos1, int islandPos2){
        Island deleteIsland,notDeleteIsland;
        int islandPosNotDelete;
        if(islandPos1<islandPos2){
            deleteIsland=this.islands.get(islandPos2);
            notDeleteIsland=this.islands.get(islandPos1);
            this.islands.remove(islandPos2);
            islandPosNotDelete=islandPos1;

        }
        else {
            deleteIsland = this.islands.get(islandPos1);
            notDeleteIsland=this.islands.get(islandPos2);
            this.islands.remove(islandPos1);
            islandPosNotDelete=islandPos2;
        }

        moveStudentsToIsland(islandPosNotDelete,deleteIsland.getStudents());
        notDeleteIsland.setNumberOfTowers(notDeleteIsland.getNumberOfTowers()+1);
        //fare i controlli sul noEntryCard.

    }
    public void moveStudentsToIsland(int islandPosition, EnumMap<Color,Integer> students )
    {
        for (Color c: Color.values()) {
            this.islands.get(islandPosition).addStudents(c,students.get(c));
        }
    }

    public void fillClouds(BoardChange bChange){
        if(players.size()==2){
            for(int i=0; i<2; i++){
                clouds.add(new Cloud());
            }
            clouds.get(0).setStudents(bChange.getStudents1());
            clouds.get(1).setStudents(bChange.getStudents2());

        }else if(players.size()==3){
            for(int i=0; i<3; i++){
                clouds.add(new Cloud());
            }
            clouds.get(0).setStudents(bChange.getStudents1());
            clouds.get(1).setStudents(bChange.getStudents2());
            clouds.get(2).setStudents(bChange.getStudents3());
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

    public Player getClientPlayer() {
        return clientPlayer;
    }

    public void moveMotherNature(int steps){
        this.motherNaturePosition=(motherNaturePosition+steps)%12;
    }
}
