package it.polimi.ingsw.Server.ServerToClient;

import it.polimi.ingsw.Controller.State.MoveTo;
import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Tower;

import java.io.Serializable;
import java.util.EnumMap;

public class BoardChange implements Serializable{
    private MoveTo moveTo;

    private Color studentColor;

    private Integer islandPosition;

    private Integer conquerIsland;

    private int motherNatureSteps;
    private Tower conquerorTower;
    private Integer mergedIsland1;
    private Integer mergedIsland2;
    private String asset=null;
    private Color color=null;
    private EnumMap<Color,Integer> entranceStudent=null;
    private EnumMap<Color,Integer> choosenStudent=null;
    private EnumMap<Color,Integer> cardStudents=null;
    private int cloud;
    private int playerID;

    private EnumMap<Color, Integer> students1 = new EnumMap<Color, Integer>(Color.class);
    private EnumMap<Color, Integer> students2= new EnumMap<Color, Integer>(Color.class);
    private EnumMap<Color, Integer> students3 = new EnumMap<Color, Integer>(Color.class); //null if only two players


    private Change change;

    public BoardChange(MoveTo moveTo, Color studentColor, int islandPosition,int playerID) {
        this.moveTo = moveTo;
        this.studentColor = studentColor;
        this.islandPosition = islandPosition;
        this.playerID=playerID;
        this.change=Change.MOVESTUDENT;
    }

    public BoardChange(int motherNatureSteps) {
        this.motherNatureSteps = motherNatureSteps;
        this.change=Change.MOTHERNATURE;
    }

    public BoardChange(Tower conquerorTower,Integer conquerIsland) {
        this.conquerorTower = conquerorTower;
        this.conquerIsland=conquerIsland;
        this.change=Change.CONQUER;
    }

    public BoardChange(Tower conquerorTower,Integer conquerIsland,Integer mergedIsland1, Integer mergedIsland2) {
        this.conquerorTower = conquerorTower;
        this.conquerIsland=conquerIsland;
        this.mergedIsland1 = mergedIsland1;
        this.mergedIsland2 = mergedIsland2;
        this.change=Change.MERGE;
    }

    public BoardChange(EnumMap<Color, Integer> students1,EnumMap<Color, Integer> students2,EnumMap<Color, Integer> students3){
        this.students1=students1;
        this.students2=students2;
        this.students3=students3;
        this.change=Change.CLOUD;
    }

    public BoardChange(EnumMap<Color, Integer> students1,EnumMap<Color, Integer> students2){
        this.students1=students1;
        this.students2=students2;
        this.change=Change.CLOUD;
    }

    public BoardChange(EnumMap<Color, Integer> students1,int cloud,int player){
        this.cloud=cloud;
        this.students1=students1;
        this.playerID=player;
        this.change=Change.TAKECLOUD;
    }



    public BoardChange(String asset,Integer posIsland,Color color, EnumMap<Color,Integer> cardStudents,EnumMap<Color,Integer> choosenStudent, EnumMap<Color,Integer> entranceStudent,int playerID){
        this.asset=asset;
        this.playerID=playerID;
        this.choosenStudent=choosenStudent;
        this.islandPosition=posIsland;
        this.entranceStudent=entranceStudent;
        this.cardStudents=cardStudents;

        if(color!=null)
            this.color=color;
        switch (asset){
            case "auctioneer.jpg":
                change=Change.MOTHERNATURE;
                break;
            case "clown.jpg":
                change=Change.PLAYCLOWN;
                break;
            case "herbalist.jpg":
                change=Change.PLAYHERBALIST;
                break;
            case "innkeeper.jpg":
                change=Change.PLAYINNKEEPER;
                break;
            case "princess.jpg":
                change=Change.PLAYPRINCESS;
                break;
            case "storyteller.jpg":
                change=Change.PLAYSTORYTELLER;
                break;
            case "thief.jpg":
                change=Change.PLAYTHIEF;
                break;
            default:
                change=Change.DEFAULT;
                break;
        }

    }



    public MoveTo getMoveTo() {
        return moveTo;
    }

    public Color getStudentColor() {
        return studentColor;
    }

    public int getIslandPosition() {
        return islandPosition;
    }

    public int getMotherNatureSteps() {
        return motherNatureSteps;
    }

    public Tower getConquerorTower() {
        return conquerorTower;
    }

    public Integer getMergedIsland1() {
        return mergedIsland1;
    }

    public Integer getMergedIsland2() {
        return mergedIsland2;
    }

    public Change getChange() { return change; }

    public int getPlayer() {return playerID;}

    public Integer getConquerIsland() {return conquerIsland;}

    public EnumMap<Color, Integer> getStudents1() {
        return students1;
    }

    public EnumMap<Color, Integer> getStudents2() {
        return students2;
    }

    public EnumMap<Color, Integer> getStudents3() {
        return students3;
    }

    public int getCloud() {

        return cloud;
    }

    public void setCloud(int cloud) {

        this.cloud = cloud;
    }


    public String getAsset() {

        return asset;
    }

    public void setAsset(String asset) {

        this.asset = asset;
    }

    public Color getColor() {

        return color;
    }

    public void setColor(Color color) {

        this.color = color;
    }

    public EnumMap<Color, Integer> getEntranceStudent() {

        return entranceStudent;
    }

    public void setEntranceStudent(EnumMap<Color, Integer> entranceStudent) {

        this.entranceStudent = entranceStudent;
    }

    public EnumMap<Color, Integer> getChoosenStudent() {

        return choosenStudent;
    }

    public void setChoosenStudent(EnumMap<Color, Integer> choosenStudent) {

        this.choosenStudent = choosenStudent;
    }

    public EnumMap<Color, Integer> getCardStudents() {
        return cardStudents;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
