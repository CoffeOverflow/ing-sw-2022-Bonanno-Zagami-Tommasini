package it.polimi.ingsw.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

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
    private List<CharacterCard> cards=new ArrayList<CharacterCard>();
    private List<Cloud> clouds=new ArrayList<Cloud>();
    private Optional<Integer> coins;
    private List<Island> islands=new ArrayList<Island>();
    private EnumMap<Color,Integer> studentsBag=new EnumMap<>(Color.class);
    private EnumMap <Color,Professor> professors=new EnumMap<Color,Professor>(Color.class);
    private HashMap<String,Integer> characterCards=new HashMap<String,Integer>();
    private EnumMap <Color,Integer> studentsOnCard=new EnumMap<Color,Integer>(Color.class);



    private HashMap<Integer,AssistantCard> currentCardPlayers=new HashMap<>();
    private int currentPlayer;
    List<Color> colorsOnBag = new ArrayList<Color>();




    /**
     * Initializes the whole game based on players and expert mode
     * @param expertMode
     * @param players
     */
    public GameModel(boolean expertMode,List<Player> players) {

        //INIZIALIZZARE CHARACTER CARD

        this.numberOfPlayers=players.size();
        this.players=players;
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
            coins=Optional.of(20);

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
            if(islands.get(i)!=(islands.get(motherNaturePosition)) || islands.get(i)!=islands.get((motherNaturePosition+6)%11))
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

        /**
         * Randomly assign students to each player
         */
        for(int i=0; i<numberOfPlayers;i++)
        {
            for (Color c:Color.values())
                entryStudentPerPlayer.put(c,0);
            for(int j=0;j<numberOfStudent;j++)
            {
                Color col=colorValues.get(rand.nextInt(colorValues.size()));
                if(studentsBag.get(col)==1)
                {
                    colorValues.remove(col);
                }
                studentsBag.put(col,studentsBag.get(col)-1);
                entryStudentPerPlayer.put(col,entryStudentPerPlayer.get(col)+1);
            }

            getPlayerByID(i).setEntryStudents(entryStudentPerPlayer);

        }
        for(Color c:Color.values())
            colorsOnBag.add(c);

    }

    /**
     * Unify the islands and delete the one with the lowest index from the array list
     * @param islandPos1
     * @param islandPos2
     */
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

    /**
     * Move the students to the island by passing an enum map
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
     * ove the students to the island by passing the student's color
     * @param islandPosition
     * @param student
     */

    public void moveStudentToIsland(int islandPosition, Color student )
    {
        this.islands.get(islandPosition).addStudents(student,1);
    }

    public void useCharacterCard(){
        //MANCA QUESTA
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
                if (getPlayerByID(player).equals(this.professors.get(c).getPlayer())) {
                    influence += this.islands.get(island).getStudentsOf(c);
                 }
            }
            return influence;
    }

    /**
     * Add
     * @param player
     * @param studentColor
     */
    public void moveToSchool (int player,Color studentColor){
        getPlayerByID(player).addStudentOf(studentColor);
        int numOfColor=getPlayerByID(player).getStudentsOf(studentColor);
        int max=0;
        int idMax=-1;
        for(Player p: players)
        {
            if(!p.equals(getPlayerByID(player))){
                if(p.getStudentsOf(studentColor)>max)
                {
                    max=p.getStudentsOf(studentColor);
                    idMax=p.getPlayerID();
                }
            }
        }
        if(numOfColor>max)
        {
            getPlayerByID(player).addProfessor(studentColor);
            getPlayerByID(idMax).removeProfessor(studentColor);
        }
    }

    /**
     * Get random students from the Bag and fill clouds
     * @return
     */
    public boolean getStudentsFromBag()  {
        EnumMap<Color,Integer> studentsOnClouds=new EnumMap<Color, Integer>(Color.class);

        Random rand=new Random();
        Color col;

        for(int i=0;i<clouds.size();i++){
            for (Color c: Color.values())
                studentsOnClouds.put(c,0);
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
                    return false;
                }

                }
            fillCloud(studentsOnClouds,i);
            }
        return true;
        }

    public void fillCloud(EnumMap<Color,Integer> students,int cloud){
        this.clouds.get(cloud).setStudents(students);
    }

    public void moveMotherNature(int steps){
        this.motherNaturePosition+=steps;
    }

    public int getMotherNaturePosition(){
        return this.motherNaturePosition;
    }

    public void setTowerOnIsland(int island,int player)
    {
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
}

