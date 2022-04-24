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



    /**
     * Initializes the whole game based on the number of players and expert mode
     * @param expertMode
     * @param players
     */
    public GameModel(boolean expertMode,List<Player> players) {

        //INIZIALIZZARE CHARACTER CARD PRENDENDO IL SUO VALORE TRAMITE UN HASHMAP <NOME,COSTO>

        this.numberOfPlayers=players.size();
        this.players=players;
        this.expertMode=expertMode;
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

        List<String> charcacterName=new ArrayList<>();
        charcacterName.add("CarteTOT_front");

        for(int i=2;i<13;i++){
            charcacterName.add("CarteTOT_front"+i);
        }

        List<Integer> cost=Arrays.asList(1,3,1,2,3,1,2,3,1,2,3,2);
        List<Integer> charcaterCost = new ArrayList<>(cost);

        Random num=new Random();
        this.motherNaturePosition=num.nextInt(12);
        EnumMap<Color,Integer> colorToIsland=new EnumMap<>(Color.class);
        for(Color c: Color.values()){
            colorToIsland.put(c,2);
        }
        List<Color> colorValues = new ArrayList<Color>();
        for(Color c:Color.values())
            colorValues.add(c);

        Random rand=new Random();

      /*  for(int i=0;i<3;i++)
        {
            int randNum=rand.nextInt(charcacterName.size());
            characterCards.put(charcacterName.get(i),charcaterCost.get(i));
            CharacterCard chard=new CharacterCard(charcaterCost.get(i),charcacterName.get(i),studentsOnCard);
            charcacterName.remove(i);
            charcaterCost.remove(i);
        }*/
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

        for(Color c: Color.values()){
            studentsBag.put(c,24);
        }
        for(Color c:Color.values())
            colorValues.add(c);

        EnumMap<Color,Integer> entryStudentPerPlayer=new EnumMap<Color, Integer>(Color.class);

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
            this.players.get(i).setEntryStudents(entryStudentPerPlayer);

        }

    }

    /**
     *
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

    public void moveStudentsToIsland(int islandPosition, EnumMap<Color,Integer> students )
    {
        for (Color c: Color.values()) {
            this.islands.get(islandPosition).addStudents(c,students.get(c));
        }
    }

    public void useCharacterCard(){
        //MANCA QUESTA
    }

    public void useAssistantCard(int player, String card){
        this.players.get(player).useAssistantCard(card);
    }

    public void chooseCloud(int player, int cloud)
    {
        this.players.get(player).addEntryStudents(clouds.get(cloud).getStudents());
    }

    public int getPlayerInfluence(int player,int island)
    {
        int influence=0;
        for(Color c: Color.values()){
            if(this.players.get(player).equals(this.professors.get(c).getPlayer())){
                   influence+=this.islands.get(island).getStudentsOf(c);
            }
        }
        return influence;
    }
    public void moveToSchool (int player,Color studentColor){
        this.players.get(player).addStudentOf(studentColor);
    }

    public void getStudentsFromBag()  {
        EnumMap<Color,Integer> studentsOnClouds=new EnumMap<Color, Integer>(Color.class);
        List<Color> colorValues = new ArrayList<Color>();
        Random rand=new Random();
        Color col;
        for(Color c:Color.values())
            colorValues.add(c);

        for(int i=0;i<clouds.size();i++){
            for (Color c: Color.values())
                studentsOnClouds.put(c,0);
            for(int j=0;j<numberOfStudentBag;j++)
            {
                try {
                    col = colorValues.get(rand.nextInt(colorValues.size()));
                    if(studentsBag.get(col)==1)
                        colorValues.remove(col);
                    studentsBag.put(col,studentsBag.get(col)-1);
                    studentsOnClouds.put(col,studentsOnClouds.get(col)+1);
                }
                catch (IllegalArgumentException e){
                    System.out.println("There are no more students in the bag");
                }

                }
            fillCloud(studentsOnClouds,i);
            }
        }

    public void moveMotherNature(int steps){
        this.motherNaturePosition+=steps;
    }

    public int getMotherNaturePosition(){
        return this.motherNaturePosition;
    }

    public void setTowerOnIsland(int island,int player)
    {
        this.islands.get(island).setTower(players.get(player).getTower());
    }

    private void fillCloud(EnumMap<Color,Integer> students,int cloud){
        this.clouds.get(cloud).setStudents(students);
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
}

