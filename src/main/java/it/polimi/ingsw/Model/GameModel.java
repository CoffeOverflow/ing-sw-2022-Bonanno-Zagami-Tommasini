package it.polimi.ingsw.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.Controller.GameController;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Array;
import java.util.*;

/**
 *
 * @author Giuseppe Bonanno
 *
 * */

public class GameModel {

    private int numberOfPlayers,numberOfStudent,numberOfTowers,motherNaturePosition,numberOfStudentBag;
    private boolean expertMode;
    private List<Player> players=new ArrayList<Player>();
    private List<CharacterCard> characterCards=new ArrayList<CharacterCard>();


    private boolean lastRound =false;



    private boolean emptyClouds =false;

    private HashMap<String,Integer> charactersPositions=new HashMap<>();
    private List<Cloud> clouds=new ArrayList<Cloud>();
    private static Optional<Integer> coins;
    private List<Island> islands=new ArrayList<Island>();
    private EnumMap<Color,Integer> studentsBag=new EnumMap<>(Color.class);
    private EnumMap <Color,Professor> professors=new EnumMap<Color,Professor>(Color.class);

    private boolean twoAdditionalSteps=false;
    private boolean twoAdditionalPoints=false;
    private Color notCountedColor=null;
    private boolean towersNotCounted=false;
    private boolean takeProfessorWhenTie=false;

    private boolean[] firstUseCharacters=new boolean[3];

    private HashMap<Integer,AssistantCard> currentCardPlayers=new HashMap<>();
    private int currentPlayer;
    List<Color> colorsOnBag = new ArrayList<Color>();

    private Conquest conquest=null;



    /**
     * Initializes the whole game based on players and expert mode
     * @param expertMode
     * @param numberOfPlayers
     */
    public GameModel(boolean expertMode,int numberOfPlayers) {

        this.numberOfPlayers=numberOfPlayers;
        this.expertMode=expertMode;

        /**
         * setting parameters based on the number of players
         */
        if(numberOfPlayers==2 || numberOfPlayers==4){
            this.numberOfStudent=7;
            this.numberOfTowers=8;
            this.numberOfStudentBag=3;
        }
        else
        {
            this.numberOfStudent=9;
            this.numberOfTowers=6;
            this.numberOfStudentBag=4;
        }

        if(expertMode)
            coins=Optional.of(20-numberOfPlayers);

        /**
         * Initialize clouds, islands and professor
         */
        for(int i=0;i<numberOfPlayers;i++)
        {
            Cloud c=new Cloud();
            clouds.add(c);
        }
        for(int i=0;i<12;i++) {
            Island isl = new Island();
            islands.add(isl);
        }
        for(Color c:Color.values()){
            Professor p=new Professor(c);
            professors.put(c,p);
        }


        /**
         * Set a random position for mother nature
         */
        Random rand=new Random();

        this.motherNaturePosition=rand.nextInt(12);

        EnumMap<Color,Integer> colorToIsland=new EnumMap<>(Color.class);

        for(Color c: Color.values()){
            colorToIsland.put(c,2);
        }

        List<Color> colorValues = new ArrayList<Color>();
        for(Color c:Color.values())
            colorValues.add(c);

        /**
         * Put a random student on the islands except the one where mother nature is and the one opposite her
         */
        for(int i=0;i<12;i++)
        {
            if(islands.get(i)!=(islands.get(motherNaturePosition)) && islands.get(i)!=islands.get((motherNaturePosition+6)%11))
            {
               try {
                   Color col = colorValues.get(rand.nextInt(colorValues.size()));
                   if (colorToIsland.get(col) == 1) {
                       colorValues.remove(col);
                   } else
                       colorToIsland.put(col, 1);
                   islands.get(i).addStudents(col, 1);
               }
               catch (IllegalArgumentException e){
                   System.out.println("There are no more students in the bag");
               }
            }

        }

        /**
         * Fill the Bag of the game with the remaining students
         */
        for(Color c: Color.values()){
            studentsBag.put(c,24);
        }

        for(Color c:Color.values())
            colorValues.add(c);

        EnumMap<Color,Integer> entryStudentPerPlayer=new EnumMap<Color, Integer>(Color.class);

        for(Color c:Color.values())
            colorsOnBag.add(c);

        /**
         * Initialize character cards
         */
        String[] characterAssets={"innkeeper.jpg","auctioneer.jpg","postman.jpg","herbalist.jpg","centaur.jpg",
                "clown.jpg", "infantryman.jpg", "lumberjack.jpg", "storyteller.jpg","princess.jpg","thief.jpg","merchant.jpg"};
        int[] cardNumbers=new int[3];
        for(int i=0; i<3; i++){
            switch(i){
                case 0:
                    cardNumbers[i]=rand.nextInt(12);
                    break;
                case 1:
                    do{
                        cardNumbers[i]=rand.nextInt(12);
                    }while(cardNumbers[i]==cardNumbers[0]);
                    break;
                case 2:
                    do{
                        cardNumbers[i]=rand.nextInt(12);
                    }while(cardNumbers[i]==cardNumbers[0] || cardNumbers[i]==cardNumbers[1]);
                    break;
            }
        }
        if(expertMode){
            try{
            for(int i=0; i<3;i++) {
                EnumMap<Color, Integer> students=new EnumMap<>(Color.class);
                if(cardNumbers[i]==0 || cardNumbers[i]==9){
                    students=getStudentsFromBag(4);
                    characterCards.add(new CharacterCard(characterAssets[cardNumbers[i]],students));
                }
                else if(cardNumbers[i]==5){
                    students=getStudentsFromBag(6);
                    characterCards.add(new CharacterCard(characterAssets[cardNumbers[i]],students));

                }
                else{
                    characterCards.add(new CharacterCard(characterAssets[cardNumbers[i]]));
                }
                charactersPositions.put(characterAssets[cardNumbers[i]],i);
            }}catch(Exception e){
                e.printStackTrace();
            }
        }

    }
    public void printBag(){
        System.out.println(studentsBag.toString());
    }

    public synchronized void addPlayer(int id,String nickname){
        Tower towers=Tower.values()[this.players.size()];
        Player player = new Player(id,nickname,this.expertMode,towers,this.numberOfTowers);
        this.players.add(player);
        getPlayerByID(id).setEntryStudents(getStudentsFromBag(numberOfStudent));
    }
    /**
     * Unify the islands and delete the one with the lowest index from the array list
     * @param islandPos1
     * @param islandPos2
     */
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

    }

    /**
     * Move the students to the island by passing an enum map
     * @param islandPosition
     * @param students
     */
    public void moveStudentsToIsland(int islandPosition, EnumMap<Color,Integer> students )
    {
        for (Color c: students.keySet()) {
            this.islands.get(islandPosition).addStudents(c,students.get(c));
        }
    }

    /**
     * Move the students to the island by passing the student's color
     * @param islandPosition
     * @param student
     */

    public void moveStudentToIsland(int islandPosition, Color student )
    {
        this.islands.get(islandPosition).addStudents(student,1);
    }

    /**
     * Use the assistant card with the name inside the card parameter
     * @param player
     * @param card
     */
    public void useAssistantCard(int player, String card)  {
        getPlayerByID(player).useAssistantCard(card);
    }

    /**
     * Choose the cloud once turn is over
     * @param player
     * @param cloud
     */
    public void chooseCloud(int player, int cloud)
    {
        getPlayerByID(player).addEntryStudents(clouds.get(cloud).getStudents());
    }

    /**
     * Calculate and return the player influence
     * @param player
     * @param island
     * @return
     */
    public int getPlayerInfluence(int player,int island)
    {
        int influence = 0;
        for (Color c : Color.values()) {
            if(!c.equals(notCountedColor)){
                if (getPlayerByID(player).equals(this.professors.get(c).getPlayer())) {
                    influence += this.islands.get(island).getStudentsOf(c);
                }
            }
        }
        if(player==currentPlayer && twoAdditionalPoints){
            influence+=2;
        }
        return influence;
    }

    public Conquest computeInfluence(int islandPosition){
        //take control of the island:
        int key=0;
        HashMap<Integer, Integer> influences=new HashMap<>();
        Optional<Integer> conqueror=Optional.empty();
        for(Player p:players){
            if(!towersNotCounted && getTowerOnIsland(islandPosition).isPresent() &&
                    getTowerOnIsland(islandPosition).get().equals(getPlayerTower(p.getPlayerID()))){
                influences.put(p.getPlayerID(),getPlayerInfluence(p.getPlayerID(),islandPosition)
                        + getIslandByPosition(islandPosition).getNumberOfTowers());
            }else{
                influences.put(p.getPlayerID(),getPlayerInfluence(p.getPlayerID(),islandPosition));
            }
            if(influences.get(p.getPlayerID())>key){
                key=influences.get(p.getPlayerID());
                conqueror=Optional.of(p.getPlayerID());
            }
        }
        System.out.println("DEBUG CI 0");
        //check if the higher value of influence is unique and if the island wasn't already his
        if(conqueror.isPresent()){
            for(Player p:players){
                if(p.getPlayerID()!=conqueror.get() && influences.get(p.getPlayerID()).equals(influences.get(conqueror.get()))){
                    conqueror=Optional.empty();
                    break;
                }else if(p.getPlayerID()==conqueror.get() && islands.get(islandPosition).getTower().isPresent()
                        &&  islands.get(islandPosition).getTower().get().equals(p.getTower())){
                    conqueror=Optional.empty();
                    break;
                }
            }
        }
        System.out.println("DEBUG CI 1");
        int mergeResult=0;
        int oldIslandsSize=islands.size();
        //if the value is unique, conquer the island

        if(conqueror.isPresent()){
            Optional<Tower> oldTower= getTowerOnIsland(islandPosition);
            setTowerOnIsland(islandPosition,conqueror.get());
            if(oldTower.isPresent() && !getPlayerByTower(oldTower.get()).equals(getPlayerByID(conqueror.get())) ){
                int oldNumberOfTower=getPlayerByTower(oldTower.get()).getNumberOfTower();
                getPlayerByTower(oldTower.get()).setNumberOfTower(oldNumberOfTower+islands.get(islandPosition).getNumberOfTowers());
            }
            getPlayerByID(conqueror.get()).buildTower(islands.get(islandPosition).getNumberOfTowers());

           mergeResult= checkMergeIsland(islandPosition,
                    getPlayerTower(conqueror.get()));
        }
        System.out.println("DEBUG CI 2");
        Conquest conquest;
        int mergeIsland1=0;
        int mergeIsland2=0;
        if(conqueror.isPresent() && mergeResult==0)
            return new Conquest(getPlayerTower(conqueror.get()),islandPosition,null,null);
        else if(conqueror.isPresent() && mergeResult==-1){
            if(islandPosition==0)
                mergeIsland1=oldIslandsSize-1;
            else {
                mergeIsland1 = islandPosition - 1;
                if(motherNaturePosition==islandPosition)
                motherNaturePosition--;
            }
            if(islands.size()==oldIslandsSize-1){
                return new Conquest(getPlayerTower(conqueror.get()),islandPosition,mergeIsland1,null);
            }else if(islands.size()==oldIslandsSize-2){
                if(islandPosition==oldIslandsSize-1)
                    mergeIsland2=0;
                else mergeIsland2=islandPosition+1;
                return new Conquest(getPlayerTower(conqueror.get()),islandPosition,mergeIsland1,mergeIsland2);
            }
        }else if(conqueror.isPresent() && mergeResult==+1){
            if(islandPosition==oldIslandsSize-1) {
                mergeIsland1 = 0;
                if(motherNaturePosition==islandPosition)
                motherNaturePosition=0;
            }
            else mergeIsland1=islandPosition+1;
            if(islands.size()==oldIslandsSize-1){
                return new Conquest(getPlayerTower(conqueror.get()),islandPosition,mergeIsland1,null);
            }else if(islands.size()==oldIslandsSize-2){
                if(islandPosition==0)
                    mergeIsland2=oldIslandsSize-1;
                else mergeIsland2=islandPosition-1;
                return new Conquest(getPlayerTower(conqueror.get()),islandPosition,mergeIsland1,mergeIsland2);
            }
        }else return null;
    return null;
    }

    public int checkMergeIsland( int island, Tower tower){
        if(island==getIslandSize()-1 && getTowerOnIsland(island-1).isPresent() && getTowerOnIsland(island-1).get().equals(tower)){
            mergeIslands(island-1,island,tower);
            checkMergeIsland( island-1,tower);
            return -1;
        }else if(island==getIslandSize()-1 && getTowerOnIsland(0).isPresent() && getTowerOnIsland(0).get().equals(tower) ){
            mergeIslands(0,island,tower);
            checkMergeIsland(0,tower);
            return +1;
        }else if(island==0 && getTowerOnIsland(getIslandSize()-1).isPresent() && getTowerOnIsland(getIslandSize()-1).get().equals(tower)){
            mergeIslands(island,getIslandSize()-1,tower);
            checkMergeIsland( island,tower);
            return -1;
        }else if((island-1)>=0 && getTowerOnIsland(island-1).isPresent() && getTowerOnIsland(island-1).get().equals(tower)){
            mergeIslands(island-1,island,tower);
            checkMergeIsland( island-1,tower);
            return -1;
        }else if((island+1)<getIslandSize() && getTowerOnIsland(island+1).isPresent() && getTowerOnIsland(island+1).get().equals(tower)){
            mergeIslands(island,island+1,tower);
            checkMergeIsland(island,tower);
            return +1;
        }else{return 0;}



    }

    /**
     * Add
     * @param player
     * @param studentColor
     */
    public void moveToSchool (int player,Color studentColor){
        getPlayerByID(player).addStudentOf(studentColor);
        getPlayerByID(player).removeEntryStudent(studentColor);
        int numOfColor=getPlayerByID(player).getStudentsOf(studentColor);
        int max=0;
        for(Player p: players)
        {
            if(!p.equals(getPlayerByID(player))){
                if(p.getStudentsOf(studentColor)>max)
                {
                    max=p.getStudentsOf(studentColor);
                }
            }
        }
        if((!takeProfessorWhenTie && numOfColor>max) || (takeProfessorWhenTie && numOfColor>=max) )
        {
            this.professors.get(studentColor).goToSchool(getPlayerByID(player));
        }
        if(getPlayerByID(player).getStudentsOf(studentColor) % 3 == 0 && expertMode){
            if(coins.get() > 0){
                coins = Optional.of(coins.get() - 1);
                getPlayerByID(player).addMoney();
            }
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

        if ( null!=toGo &&!toGo.equals(professors.get(studentColor).getPlayer()))
            this.professors.get(studentColor).goToSchool(toGo);


        if(professors.get(studentColor).getPlayer().getPlayerID()==player && professors.get(studentColor).getPlayer().getStudentsOf(studentColor)==0)
            professors.get(studentColor).getPlayer().removeProfessor(studentColor);

    }

    /**
     * Get random students from the Bag and fill clouds
     * @return
     */
    public boolean getStudentsFromBag()  {
        int numStudents=0;
        for(int i=0;i<clouds.size();i++){
            EnumMap<Color,Integer> studentsOnClouds=new EnumMap<Color, Integer>(Color.class);
            for (Color c: Color.values())
                studentsOnClouds.put(c,0);
            Random rand=new Random();
            Color col;
            for(int j=0;j<numberOfStudentBag;j++)
            {
                try {
                    col = colorsOnBag.get(rand.nextInt(colorsOnBag.size()));
                    if(studentsBag.get(col)==1)
                        colorsOnBag.remove(col);
                    studentsBag.put(col,studentsBag.get(col)-1);
                    studentsOnClouds.put(col,studentsOnClouds.get(col)+1);
                }
                catch (IllegalArgumentException e){
                    System.out.println("There are no more students in the bag");
                    lastRound=true;
                    emptyClouds=true;
                    return false;
                }
            }
            if(!lastRound)
                fillCloud(studentsOnClouds,i);
            }
        return true;
        }

    public void fillCloud(EnumMap<Color,Integer> students,int cloud){
        this.clouds.get(cloud).setStudents(students);
    }

    public void moveMotherNature(int steps){
        this.motherNaturePosition=(motherNaturePosition+steps)%this.islands.size();}

    public void endTurnOfPlayer(){
        this.towersNotCounted=false;
        this.notCountedColor=null;
        this.twoAdditionalPoints=false;
        this.twoAdditionalSteps=false;
        this.takeProfessorWhenTie=false;
    }

    public int getMotherNaturePosition(){
        return this.motherNaturePosition;
    }

    public void setTowerOnIsland(int island,int player) {
            this.islands.get(island).setTower(getPlayerByID(player).getTower());
    }


    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getNumberOfStudent() {
        return numberOfStudent;
    }

    public int getNumberOfTowers() {
        return numberOfTowers;
    }

    public int getNumberOfStudentBag() {
        return numberOfStudentBag;
    }

    public boolean isExpertMode() {
        return expertMode;
    }
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public HashMap<Integer, AssistantCard> getCurrentCardPlayers() {
        return currentCardPlayers;
    }

    public void setCurrentCardPlayers(HashMap<Integer, AssistantCard> currentCardPlayers) {
        this.currentCardPlayers = currentCardPlayers;
    }

    public Optional<Tower> getTowerOnIsland(int islandPos)
    {
        return this.islands.get(islandPos).getTower();
    }

    public Tower getPlayerTower(int player){

           return getPlayerByID(player).getTower();

    }

    public boolean isPresentEntryPlayer(Color c){
        return getPlayerByID(currentPlayer).studentIsPresent(c);
    }

    public void removeEntryStudents(Color c){
        getPlayerByID(currentPlayer).removeEntryStudent(c);
    }

    public int getIslandSize(){
        return islands.size();
    }

    /**
     * Take and return students from cloud and clear it.
     * @param cloud
     * @return
     */
    public EnumMap<Color,Integer> takeStudentsFromCloud(int cloud){
        EnumMap<Color,Integer> student= this.clouds.get(cloud).getStudents();
        EnumMap<Color,Integer> empty=new EnumMap<Color, Integer>(Color.class);
        for(Color c:Color.values())
            empty.put(c,0);
        this.clouds.get(cloud).setStudents(empty);
        return student;
    }

    public boolean areStudentsOnCloud (int cloud){
        EnumMap<Color,Integer> students=this.clouds.get(cloud).getStudents();
        for(Color c:Color.values())
            if(students.get(c)!=0)
                return true;
        return false;
    }

    public void addStudentsBag(Color c, int n){
        numberOfStudentBag+=n;
        int numberBefore=studentsBag.get(c);
        studentsBag.put(c,numberBefore+n);
    }

    public Player getPlayerByTower(Tower tower){
        Player ret=null;
        for(Player p:players){
            if(p.getTower().equals(tower))
                ret=p;
        }
        return ret;
    }

    public Island getIslandByPosition(int position){
        return this.islands.get(position);
    }

    public Player getPlayerByID(int id)  {
        for(Player p:this.players)
            if(p.getPlayerID()==id)
                return p;
        return null;
    }

    public EnumMap<Color, Professor> getProfessors() {
        return professors;
    }

    public boolean isTwoAdditionalSteps() {
        return twoAdditionalSteps;
    }

    public void setTwoAdditionalSteps(boolean twoAdditionalSteps) {
        this.twoAdditionalSteps = twoAdditionalSteps;
    }

    public boolean isTwoAdditionalPoints() {
        return twoAdditionalPoints;
    }

    public void setTwoAdditionalPoints(boolean twoAdditionalPoints) {
        this.twoAdditionalPoints = twoAdditionalPoints;
    }

    public Color getNotCountedColor() {
        return notCountedColor;
    }

    public void setNotCountedColor(Color notCountedColor) {
        this.notCountedColor = notCountedColor;
    }

    public boolean isTowersNotCounted() {
        return towersNotCounted;
    }
    public boolean isEmptyClouds() {
        return emptyClouds;
    }

    public void setTowersNotCounted(boolean towersNotCounted) {
        this.towersNotCounted = towersNotCounted;
    }

    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    public HashMap<String, Integer> getCharactersPositions() {
        return charactersPositions;
    }

    public boolean[] getFirstUseCharacters() {
        return firstUseCharacters;
    }

    public void setFirstUseCharacters(int position){
        firstUseCharacters[position]=true;
    }

    public EnumMap<Color,Integer> getStudentsFromBag(int numStudent){
        EnumMap<Color,Integer> studentsFromBag=new EnumMap<Color, Integer>(Color.class);

        Random rand=new Random();
        Color col;

        for (Color c: Color.values())
            studentsFromBag.put(c,0);

        for(int j=0;j<numStudent;j++)
            {
                try {
                    col = colorsOnBag.get(rand.nextInt(colorsOnBag.size()));
                    if(studentsBag.get(col)==1)
                        colorsOnBag.remove(col);
                    studentsBag.put(col,studentsBag.get(col)-1);
                    studentsFromBag.put(col,studentsFromBag.get(col)+1);
                }
                catch (IllegalArgumentException e){
                    System.out.println("There are no more students in the bag");
                    return null;
                }
            }
        return studentsFromBag;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public List<Player> getWinner(){
        Player winner = null;
        Player winner2 = null;
        ArrayList<Player> winners=new ArrayList<>();
        HashMap<Player,Integer> mapPlayerNumTowers=new HashMap<>();
        for(Player player : players){
            mapPlayerNumTowers.put(player,player.getNumberOfTower());
        }
        int tower = 99;
        for(Player player : mapPlayerNumTowers.keySet()){
            if(mapPlayerNumTowers.get(player) < tower){
                winner = player;
                if(winner2!=null) winner2=null;
                tower = mapPlayerNumTowers.get(player);
            }
            else if(mapPlayerNumTowers.get(player) == tower){
                int numProfWinner = 0;
                int numProfP = 0;
                for(Professor pr: professors.values()){
                    if(pr.getPlayer().equals(player))
                        numProfP++;
                    else if(pr.getPlayer().equals(winner))
                        numProfWinner++;
                }
                if(numProfP>numProfWinner) {
                    winner = player;
                    if(winner2!=null) winner2=null;
                }else if(numProfP==numProfWinner){
                    winner2=player;
                }
            }
        }
        winners.add(winner);
        if(winner2!=null) winners.add(winner2);
        return winners;
    }
    public boolean isLastRound() {return lastRound;}

    public void setLastRound(boolean lastRound) {this.lastRound = lastRound;}

    public Conquest getConquest() {
        return conquest;
    }

    public void setConquest(Conquest conquest) {
        this.conquest = conquest;
    }

    public void setTakeProfessorWhenTie(boolean takeProfessorWhenTie) {
        this.takeProfessorWhenTie = takeProfessorWhenTie;
    }
}

