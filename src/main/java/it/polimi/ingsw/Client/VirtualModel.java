package it.polimi.ingsw.Client;

import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Server.ServerToClient.*;

import java.util.*;

/**
 * @author Giuseppe Bonanno, Federica Tommasini
 * This class represents a copy of the main attributes of the model that each client will have,
 * the values will be set when the game starts and will be modified by the update messages
 */
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

    private boolean auctioneerPlayed=false;

    /**
     * Return the Player with the specific id
     * @param id
     * @return
     */
    public Player getPlayerByID(int id)  {
        for(Player p:this.players)
            if(p.getPlayerID()==id)
                return p;
        return null;
    }

    /**
     * Constructor of the class
     */
    public VirtualModel() {
        for(Color c:Color.values()){
            Professor p=new Professor(c);
            professors.put(c,p);
        }
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    /**
     * set in the virtual model the initial configuration of the islands
     * (one student per island and the position of mother nature)
     * @param msg message containing a map associating colors and position of the islands and an integer for the postion
     *            of mother nature
     */
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

    /**
     * set values in the virtual model for the nicknames, wizards and towers of the players
     * @param msg message containing info about the players
     */
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

    /**
     * set in the virtual model the initial configuration of the school (entrance students)
     * @param msg message containing students of the school and the id of the player
     */
    public void setSchoolStudents(SetUpSchoolStudent msg){
        for(Player p: players){
            if(p.getPlayerID()==msg.getPlayerID())
                p.setEntryStudents(msg.getEntranceStudents());
        }
    }

    /**
     * set in the virtual model the character cards randomly selected at the beginning of the match
     * @param msg message containing an array of strings corresponding to the assets of the cards
     */
    public void setCharacterCards(SetUpCharacterCard msg){
        characterCards=new ArrayList<>();
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

    /**
     * @see GameModel
     * @param islandPos1
     * @param islandPos2
     * @param tower
     */
    public void mergeIslands(int islandPos1, int islandPos2,Tower tower){
        Island deleteIsland,notDeleteIsland;
        int maxNumberOfTower;
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
        maxNumberOfTower= Math.max(notDeleteIsland.getNumberOfTowers(), deleteIsland.getNumberOfTowers());

        moveStudentsToIsland(islandPosNotDelete,deleteIsland.getStudents());
        if(notDeleteIsland.getNumberOfTowers()==0 || deleteIsland.getNumberOfTowers()==0)
            notDeleteIsland.setNumberOfTowers(maxNumberOfTower+1);
        else notDeleteIsland.setNumberOfTowers(notDeleteIsland.getNumberOfTowers()+ deleteIsland.getNumberOfTowers());
        if(!auctioneerPlayed)
            motherNaturePosition=islandPosNotDelete;
        else auctioneerPlayed=false;

    }

    /**
     * @see GameModel
     * @param islandPosition
     * @param students
     */
    public void moveStudentsToIsland(int islandPosition, EnumMap<Color,Integer> students )
    {
        for (Color c: Color.values()) {
            this.islands.get(islandPosition).addStudents(c,students.get(c));
        }
    }

    /**
     * @see GameModel
     * @param player
     * @param studentColor
     */
    public void moveToSchool (int player,Color studentColor){
        for(Player p:this.players)
            if(p.getPlayerID()==player)
            {
                p.addStudentOf(studentColor);
                p.removeEntryStudent(studentColor);
                checkToChangeProfessor(p,studentColor);

            }
    }

    /**
     * remove some students from the school of a player and check if the professors have to be moved
     * @param player player's ID
     * @param studentColor color of the students to remove
     * @param number number of students to remove
     */
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

    /**
     * set in the virtual model the changes that have been made on the board
     * @param msg update message
     */
    public void update(UpdateMessage msg){
        BoardChange bchange=msg.getChange();
        switch(bchange.getChange()){
            case CONQUER:
                /*
                 * the update regards a conquest of an island: set the new tower on the island
                 * and give back the eventual towers of another player present on the island
                 */
                Optional<Tower> oldTower= islands.get(bchange.getConquerIsland()).getTower();
                islands.get(bchange.getConquerIsland()).setTower(bchange.getConquerorTower());
                for(Player p:players){
                    if(p.getTower().equals(bchange.getConquerorTower()))
                        p.buildTower(islands.get(bchange.getConquerIsland()).getNumberOfTowers());
                    else if(oldTower.isPresent() && p.getTower().equals(oldTower.get()))
                        p.buildTower(-islands.get(bchange.getConquerIsland()).getNumberOfTowers());
                }
                break;

            case MOVESTUDENT:
                /*
                 * the update regards the moving of a student: can be either of an island or on a school
                 */
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
                /*
                 * the update regards the conquest of an island and the merging of such island with one or two other islands
                 */
                Optional<Tower> oldTower2= islands.get(bchange.getConquerIsland()).getTower();
                islands.get(bchange.getConquerIsland()).setTower(bchange.getConquerorTower());
                for(Player p:players){
                    if(p.getTower().equals(bchange.getConquerorTower()))
                        p.buildTower(islands.get(bchange.getConquerIsland()).getNumberOfTowers());
                    else if(oldTower2.isPresent() && p.getTower().equals(oldTower2.get()))
                        p.buildTower(-islands.get(bchange.getConquerIsland()).getNumberOfTowers());
                }
                mergeIslands(bchange.getConquerIsland(), bchange.getMergedIsland1(),bchange.getConquerorTower());
                if(bchange.getMergedIsland2()!=null) {
                    int island1=bchange.getConquerIsland()==0 ? 0 : bchange.getConquerIsland() - 1;
                    int island2=bchange.getMergedIsland2()==0 ? 0 : bchange.getMergedIsland2() - 1;
                    mergeIslands(island1, island2, bchange.getConquerorTower());
                }
                break;

            case MOTHERNATURE:
                /*
                 * the update sets mother nature to a new position, if there was a no entry card on the island, it is put
                 * back on the card
                 */
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
                /*
                 * update fills the clouds with new students
                 */
                fillClouds(bchange);
                break;

            case TAKECLOUD:
                /*
                 * the update takes the student from a cloud and puts them on a school
                 */
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
                /*
                 * update that switch students from the card clown with student in the entrance of the player who played it,
                 * decreasing also his money
                 */
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
                /*
                 * update that puts a noEntry tile on an island and removes it from the card, it also decreases the money
                 * of the player who played it
                 */
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
                /*
                 * update that takes a student from the card and puts it on an island and decreases the money of the
                 * player
                 */
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
                /*
                 * update that takes a student from the card and puts it in the dining hall of the school and
                 * decreases the money of the player
                 */
                for(Color c:Color.values()) {
                    if(bchange.getChoosenStudent().containsKey(c) && bchange.getChoosenStudent().get(c) > 0)
                        for(Player p:players)
                            if(p.getPlayerID()== bchange.getPlayer()) {
                                p.addStudentOf(c);
                                checkToChangeProfessor(p,c);
                                for(CharacterCard card :characterCards)
                                    if(card.getAsset().equals(bchange.getAsset())) {
                                        card.setStudents(bchange.getCardStudents());
                                        p.decreaseMoney(card.getCost());
                                        card.increaseCost();
                                    }
                            }

                }
                break;

            case PLAYSTORYTELLER:
                /*
                 * update that switch students from the entrance of the school with students in the dining hall
                 * and decreases the money of the player
                 */
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
                /*
                 * update that removes three students of a color from all the schools
                 */
                Color colorToPutOnTheBag=bchange.getColor();
                for(Player p:players)
                {
                    p.removeThreeStudentOf(colorToPutOnTheBag);
                }
                for(Player p:players)
                    if(p.getPlayerID()== bchange.getPlayer())
                        for(CharacterCard card :characterCards)
                            if(card.getAsset().equals(bchange.getAsset())){
                                Player hasProfessor=professors.get(bchange.getColor()).getPlayer();
                                if(null!=hasProfessor && hasProfessor.getStudentsOf(bchange.getColor())==0)
                                    hasProfessor.removeProfessor(bchange.getColor());
                                p.decreaseMoney(card.getCost());
                                card.increaseCost();
                            }
                break;

            case PLAYMERCHANT:
                /*
                 * set a boolean to make the player takes the professor even with the same number of students that somebody else has
                 * and decreases the money of the player
                 */
                takeProfessorWhenTie=true;
                for(Player p:players)
                    if(p.getPlayerID()== bchange.getPlayer()){
                        for(CharacterCard card :characterCards)
                            if(card.getAsset().equals(bchange.getAsset())) {
                                p.decreaseMoney(card.getCost());
                                card.increaseCost();
                            }

                    }
                break;

            case DEFAULT:
                if(bchange.getAsset().equals("auctioneer.jpg")){
                    auctioneerPlayed=true;
                    if(this.islands.get(bchange.getIslandPosition()).getNoEntryCard()>0){
                        this.islands.get(bchange.getIslandPosition()).setNoEntryCard(this.islands.get(bchange.getIslandPosition()).getNoEntryCard()-1);
                    }
                }
                /*
                 * decreases the money of the player after he played a character card
                 */
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

    /**
     * fill the clouds of the virtual model with new students
     * @param bChange board change containing the new students for the clouds
     */
    public void fillClouds(BoardChange bChange){

        if(clouds.isEmpty()){
            for(int i=0; i< players.size(); i++)
                clouds.add(new Cloud());
        }
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

    /**
     * get an instance of the player corresponding to the client on which the view is
     * @return the player
     */
    public Player getClientPlayer() {
        return clientPlayer;
    }

    /**
     * move mother nature in the virtual model
     * @param steps number of steps of mother nature
     */
    public void moveMotherNature(int steps){
        this.motherNaturePosition=(motherNaturePosition+steps)%this.islands.size();
    }

    public boolean isUseCharacterCard() { return useCharacterCard;}

    public void setUseCharacterCard(boolean useCharacterCard) {this.useCharacterCard = useCharacterCard;}

    public int getNumOfInstance() {return numOfInstance++;}
    public void resetNumOfInstance(){this.numOfInstance=0;}

    /**
     * set a boolean variable to indicate that professor can be taken even with the same number of students of the player
     * who holds them
     * @param takeProfessorWhenTie boolean variable
     */
    public void setTakeProfessorWhenTie(boolean takeProfessorWhenTie) {
        this.takeProfessorWhenTie = takeProfessorWhenTie;
    }

    /**
     * check if a player can gain a professor of a certain color
     * @param player player
     * @param color color of the professor
     */
    public void checkToChangeProfessor(Player player,Color color){
        int numOfColor=player.getStudentsOf(color);
        int max=0;
        for(Player play: players) {
            if(play.getPlayerID()!=player.getPlayerID()){
                if(play.getStudentsOf(color)>max) {
                    max=play.getStudentsOf(color);
                }
            }
        }
        if((!takeProfessorWhenTie && numOfColor>max) || (takeProfessorWhenTie && numOfColor>=max)) {
            this.professors.get(color).goToSchool(player);
        }
    }
}
