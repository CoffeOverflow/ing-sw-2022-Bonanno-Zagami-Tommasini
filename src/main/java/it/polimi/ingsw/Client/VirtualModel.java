package it.polimi.ingsw.Client;

import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Server.ServerToClient.*;

import java.util.*;

public class VirtualModel {

    private List<Island> islands =new ArrayList<>();


    private int motherNaturePosition;

    private List<CharacterCard> characterCards=null;

    private List<Player> players=new ArrayList<>();

    private Player clientPlayer;

    private List<Cloud> clouds=new ArrayList<>();

    private EnumMap <Color,Professor> professors=new EnumMap<Color,Professor>(Color.class);

    private boolean useCharacterCard =false;

    private boolean takeProfessorWhenTie=false;




    private int numOfInstance =0;
    public Player getPlayerByID(int id)  {
        for(Player p:this.players)
            if(p.getPlayerID()==id)
                return p;
        return null;
    }

    public VirtualModel() {
        for(Color c:Color.values()){
            Professor p=new Professor(c);
            professors.put(c,p);
        }
    }

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
        characterCards=new ArrayList<>();
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

    public void mergeIslands(int islandPos1, int islandPos2,Tower tower){
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
        motherNaturePosition=islandPosNotDelete;

    }
    public void moveStudentsToIsland(int islandPosition, EnumMap<Color,Integer> students )
    {
        for (Color c: Color.values()) {
            this.islands.get(islandPosition).addStudents(c,students.get(c));
        }
    }

    public void moveToSchool (int player,Color studentColor){
        for(Player p:this.players)
            if(p.getPlayerID()==player)
            {
                p.addStudentOf(studentColor);
                p.removeEntryStudent(studentColor);
                checkToChangeProfessor(p,studentColor);

            }
    }

    public void removeFromSchool (int player,Color studentColor, int number){

        for(Player p:this.players) {
            if (p.getPlayerID() == player) {
                p.getStudents().put(studentColor, p.getStudentsOf(studentColor) - number);
            }
        }
        Player toGo=null;
        int max = 0;
        for (Player play : players) {
            if (play.getStudentsOf(studentColor) > max) {
                max = play.getStudentsOf(studentColor);
                toGo=play;
            }
        }
        if (null!=toGo && !toGo.equals(professors.get(studentColor).getPlayer()))
            this.professors.get(studentColor).goToSchool(toGo);
        if(professors.get(studentColor).getPlayer().getPlayerID()==player && professors.get(studentColor).getPlayer().getStudentsOf(studentColor)==0)
            professors.get(studentColor).getPlayer().removeProfessor(studentColor);
    }

    public void update(UpdateMessage msg){

        BoardChange bchange=msg.getChange();
        switch(bchange.getChange()){
            case CONQUER:
                islands.get(bchange.getConquerIsland()).setTower(bchange.getConquerorTower());
                for(Player p:players)
                    if(p.getTower().equals(bchange.getConquerorTower()))
                        p.setNumberOfTower(p.getNumberOfTower()-1);
                break;
            case MOVESTUDENT:
                if(bchange.getMoveTo().equals(MoveTo.ISLAND)){
                    for(Player p: players){
                        if(p.getPlayerID() == bchange.getPlayer()){
                            islands.get(bchange.getIslandPosition()).addStudents(bchange.getStudentColor(),1);
                            p.getEntryStudents().put(bchange.getStudentColor(),p.getEntryStudents().get(bchange.getStudentColor())-1);
                        }
                    }
                }
                else if(bchange.getMoveTo().equals(MoveTo.SCHOOL)){
                    for(Player p: players)
                    {
                        if(p.getPlayerID()==bchange.getPlayer()){
                            moveToSchool(p.getPlayerID(),bchange.getStudentColor());
                        }
                    }
                }
                break;
            case MERGE:
                islands.get(bchange.getConquerIsland()).setTower(bchange.getConquerorTower());
                mergeIslands(bchange.getConquerIsland(), bchange.getMergedIsland1(),bchange.getConquerorTower());
                if(bchange.getMergedIsland2()!=null) {
                    int island1=bchange.getConquerIsland()==0 ? islands.size()-1 : bchange.getConquerIsland() - 1;
                    int island2=bchange.getMergedIsland2()==0 ? islands.size()-1 : bchange.getMergedIsland2() - 1;
                    mergeIslands(island1, island2, bchange.getConquerorTower());
                }for(Player p:this.players)
                    if(p.getTower().equals(bchange.getConquerorTower()))
                        p.setNumberOfTower(p.getNumberOfTower()-1);
                break;
            case MOTHERNATURE:
                moveMotherNature(bchange.getMotherNatureSteps());
                if(islands.get(motherNaturePosition).getNoEntryCard()>0){
                    islands.get(motherNaturePosition).setNoEntryCard(islands.get(motherNaturePosition).getNoEntryCard()-1);
                    for(CharacterCard card:characterCards){
                        if(card.getAsset().equals("herbalist.jpg")){
                            int noEntryTitlesOnCard=card.getNoEntryTiles().get()+Integer.valueOf(1);
                            card.setNoEntryTiles(Optional.of(noEntryTitlesOnCard));
                        }
                    }
                }
                break;
            case CLOUD:
                fillClouds(bchange);
                break;
            case TAKECLOUD:
                for(Player p:players)
                    if(p.getPlayerID()== bchange.getPlayer())
                        System.out.println(p.getEntryStudents());
                EnumMap<Color,Integer> noStudent=new EnumMap<Color, Integer>(Color.class);
                for(Color c:Color.values())
                    noStudent.put(c,0);
                clouds.get(bchange.getCloud()).setStudents(noStudent);
                for(Player p:players)
                    if(p.getPlayerID()== bchange.getPlayer()){
                        p.addEntryStudents(bchange.getStudents1());
                    }
                break;
            case PLAYCLOWN:
                for(CharacterCard c: characterCards)
                    if(c.getAsset().equals("clown.jpg"))
                        c.setStudents(bchange.getCardStudents());
                for(Color c:Color.values()) {
                    if(bchange.getEntranceStudent().containsKey(c) && bchange.getEntranceStudent().get(c) > 0)
                        for(Player p:players)
                            if(p.getPlayerID()== bchange.getPlayer()) {
                                for(int i=0; i<bchange.getEntranceStudent().get(c);i++)
                                    p.removeEntryStudent(c);
                            }
                }
                for(Player p:players)
                    if(p.getPlayerID()== bchange.getPlayer()) {
                        p.addEntryStudents(bchange.getChoosenStudent());
                        for(CharacterCard card :characterCards)
                            if(card.getAsset().equals(bchange.getAsset()))
                            {
                                p.decreaseMoney(card.getCost());
                                card.increaseCost();
                            }

                    }
                break;
            case PLAYHERBALIST:
                islands.get(bchange.getIslandPosition()).setNoEntryCard(islands.get(bchange.getIslandPosition()).getNoEntryCard()+1);
                for(Player p:players){
                    if(p.getPlayerID()==bchange.getPlayer())
                    {
                        for(CharacterCard card :characterCards){
                            if(card.getAsset().equals(bchange.getAsset())){
                                p.decreaseMoney(card.getCost());
                                card.increaseCost();
                                int entryTitles=card.getNoEntryTiles().get();
                                card.setNoEntryTiles(Optional.of(entryTitles-1));
                            }
                        }
                    }
                }
                break;
            case PLAYINNKEEPER:
                for(Color c:Color.values())
                    if(bchange.getChoosenStudent().containsKey(c) && bchange.getChoosenStudent().get(c)>0) {
                        islands.get(bchange.getIslandPosition()).addStudents(c, 1);
                        for(CharacterCard card:characterCards){
                            if(card.getAsset().equals("innkeeper.jpg")) {
                                card.getStudents().get().put(c, card.getStudents().get().get(c) - 1);
                                card.setStudents(bchange.getCardStudents());
                            }
                        }
                    }
                for(Player p:players)
                    if(p.getPlayerID()== bchange.getPlayer())
                    {
                        for(CharacterCard card :characterCards)
                            if(card.getAsset().equals(bchange.getAsset()))
                            {
                                p.decreaseMoney(card.getCost());
                                card.increaseCost();
                            }

                    }
                break;
            case PLAYPRINCESS:
                for(Color c:Color.values()) {
                    if(bchange.getChoosenStudent().containsKey(c) && bchange.getChoosenStudent().get(c) > 0)
                        for(Player p:players)
                            if(p.getPlayerID()== bchange.getPlayer())
                            {
                                p.addStudentOf(c);
                                checkToChangeProfessor(p,c);
                                for(CharacterCard card :characterCards)
                                    if(card.getAsset().equals(bchange.getAsset()))
                                    {
                                        card.setStudents(bchange.getCardStudents());
                                        p.decreaseMoney(card.getCost());
                                        card.increaseCost();
                                    }
                            }

                }
                break;
            case PLAYSTORYTELLER:
                EnumMap<Color,Integer> salaToEntrance=new EnumMap<Color, Integer>(Color.class);
                for(Color c:Color.values())
                    salaToEntrance.put(c,0);
                for(Color c:Color.values()) {
                    if(bchange.getEntranceStudent().containsKey(c) && bchange.getEntranceStudent().get(c) > 0)
                    {
                        for(Player p:players)
                            if(p.getPlayerID()== bchange.getPlayer())
                                for(int i=0; i<bchange.getEntranceStudent().get(c) ; i++){
                                    moveToSchool(p.getPlayerID(),c);
                                }
                    }

                    if(bchange.getChoosenStudent().containsKey(c) && bchange.getChoosenStudent().get(c)>0)
                    {
                        for(Player p:players)
                            if(p.getPlayerID()== bchange.getPlayer())
                                removeFromSchool(p.getPlayerID(),c,bchange.getChoosenStudent().get(c));
                        salaToEntrance.put(c,bchange.getChoosenStudent().get(c));
                    }
                }

                for(Player p:players)
                    if(p.getPlayerID()== bchange.getPlayer())
                    {
                        p.addEntryStudents(salaToEntrance);
                        for(CharacterCard card :characterCards)
                            if(card.getAsset().equals(bchange.getAsset()))
                            {
                                p.decreaseMoney(card.getCost());
                                card.increaseCost();
                            }
                    }
                break;

            case PLAYTHIEF:
                Color colorToPutOnTheBag=bchange.getColor();
                for(Player p:players)
                {
                    p.removeThreeStudentOf(colorToPutOnTheBag);
                }
                for(Player p:players)
                    if(p.getPlayerID()== bchange.getPlayer())
                        for(CharacterCard card :characterCards)
                            if(card.getAsset().equals(bchange.getAsset()))
                            {   Player hasProfessor=professors.get(bchange.getColor()).getPlayer();
                                if(null!=hasProfessor && hasProfessor.getStudentsOf(bchange.getColor())==0)
                                    hasProfessor.removeProfessor(bchange.getColor());
                                p.decreaseMoney(card.getCost());
                                card.increaseCost();
                            }

                break;
            case PLAYMERCHANT:
                takeProfessorWhenTie=true;
                for(Player p:players)
                    if(p.getPlayerID()== bchange.getPlayer())
                    {
                        for(CharacterCard card :characterCards)
                            if(card.getAsset().equals(bchange.getAsset()))
                            {
                                p.decreaseMoney(card.getCost());
                                card.increaseCost();
                            }

                    }
                break;
            case DEFAULT:
                for(Player p:players)
                    if(p.getPlayerID()==bchange.getPlayer())
                        for(CharacterCard card :characterCards)
                            if(card.getAsset().equals(bchange.getAsset()))
                            {
                                p.decreaseMoney(card.getCost());
                                card.increaseCost();
                            }
                break;
        }
    }


    public void fillClouds(BoardChange bChange){

        if(clouds.isEmpty()){
            for(int i=0; i< players.size(); i++){
                clouds.add(new Cloud());
        }}
        if(players.size()==2){
            clouds.get(0).setStudents(bChange.getStudents1());
            clouds.get(1).setStudents(bChange.getStudents2());
        }else if(players.size()==3){
            clouds.get(0).setStudents(bChange.getStudents1());
            clouds.get(1).setStudents(bChange.getStudents2());
            clouds.get(2).setStudents(bChange.getStudents3());
        }

    }

    public List<Island> getIslands() {return islands;}

    public int getMotherNaturePosition() {return motherNaturePosition;}

    public List<CharacterCard> getCharacterCards() {return characterCards;}

    public List<Player> getPlayers() {return players;}


    public Player getClientPlayer() {
        return clientPlayer;
    }

    public void moveMotherNature(int steps){
        this.motherNaturePosition=(motherNaturePosition+steps)%this.islands.size();
    }

    public boolean isUseCharacterCard() { return useCharacterCard;}

    public void setUseCharacterCard(boolean useCharacterCard) {this.useCharacterCard = useCharacterCard;}

    public int getNumOfInstance() {return numOfInstance++;}
    public void resetNumOfInstance(){this.numOfInstance=0;}

    public void setTakeProfessorWhenTie(boolean takeProfessorWhenTie) {
        this.takeProfessorWhenTie = takeProfessorWhenTie;
    }

    public void checkToChangeProfessor(Player p,Color c){
        int numOfColor=p.getStudentsOf(c);
        int max=0;
        for(Player play: players)
        {
            if(play.getPlayerID()!=p.getPlayerID()){
                if(play.getStudentsOf(c)>max)
                {
                    max=play.getStudentsOf(c);
                }
            }
        }
        if((!takeProfessorWhenTie && numOfColor>max) || (takeProfessorWhenTie && numOfColor>=max))
        {
            this.professors.get(c).goToSchool(p);
        }
    }
}
