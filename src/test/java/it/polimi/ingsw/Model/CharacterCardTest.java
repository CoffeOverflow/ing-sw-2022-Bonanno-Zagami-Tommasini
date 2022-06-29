package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardTest {
    CharacterCard[] cards=new CharacterCard[12];
    EnumMap<Color, Integer> chosenStudents=new EnumMap<>(Color.class);
    EnumMap<Color, Integer> studentOnIsland=new EnumMap<>(Color.class);
    EnumMap<Color, Integer> entranceStudents=new EnumMap<>(Color.class);
    EnumMap<Color, Integer> entranceStudentsSwap=new EnumMap<Color, Integer>(Color.class);
    GameModel gm;

    @BeforeEach
    void testCharacterCard(){
        String[] characterAssets={"innkeeper.jpg","auctioneer.jpg","postman.jpg","herbalist.jpg","centaur.jpg",
                "clown.jpg", "infantryman.jpg", "lumberjack.jpg", "storyteller.jpg","princess.jpg","thief.jpg","merchant.jpg"};
        for(int i=0; i<12;i++) {
            if(i==0 || i==9){
                EnumMap<Color, Integer> students=new EnumMap<>(Color.class);
                students.put(Color.BLUE,2);
                students.put(Color.GREEN,1);
                students.put(Color.PINK,1);
                cards[i] = new CharacterCard(characterAssets[i],students);
            }else if(i==5){
                EnumMap<Color, Integer> students=new EnumMap<>(Color.class);;
                students.put(Color.BLUE,2);
                students.put(Color.GREEN,1);
                students.put(Color.PINK,1);
                students.put(Color.RED,2);
                cards[i] = new CharacterCard(characterAssets[i],students);
            }else{
                cards[i] = new CharacterCard(characterAssets[i]);
            }
        }

        gm=new GameModel(true,2);
        gm.addPlayer(1,"aaa");
        gm.addPlayer(2,"bbb");
        if(!gm.getPlayerByID(1).getEntryStudents().containsKey(Color.BLUE)
                || gm.getPlayerByID(1).getEntryStudents().get(Color.BLUE)==0){
            EnumMap<Color, Integer> studentToAdd=new EnumMap<>(Color.class);
            studentToAdd.put(Color.BLUE,1);
            gm.getPlayerByID(1).addEntryStudents(studentToAdd);
        }
        gm.moveToSchool(1,Color.BLUE);

        if(!gm.getPlayerByID(1).getEntryStudents().containsKey(Color.RED)
                || gm.getPlayerByID(1).getEntryStudents().get(Color.RED)==0){
            EnumMap<Color, Integer> studentToAdd=new EnumMap<>(Color.class);
            studentToAdd.put(Color.RED,1);
            gm.getPlayerByID(1).addEntryStudents(studentToAdd);
        }
        gm.moveToSchool(1,Color.RED);

        if(!gm.getPlayerByID(1).getEntryStudents().containsKey(Color.GREEN)
                || gm.getPlayerByID(1).getEntryStudents().get(Color.GREEN)==0){
            EnumMap<Color, Integer> studentToAdd=new EnumMap<>(Color.class);
            studentToAdd.put(Color.GREEN,1);
            gm.getPlayerByID(1).addEntryStudents(studentToAdd);
        }
        gm.moveToSchool(1,Color.GREEN);

        if(!gm.getPlayerByID(2).getEntryStudents().containsKey(Color.PINK)
                || gm.getPlayerByID(2).getEntryStudents().get(Color.PINK)==0){
            EnumMap<Color, Integer> studentToAdd=new EnumMap<>(Color.class);
            studentToAdd.put(Color.PINK,1);
            gm.getPlayerByID(2).addEntryStudents(studentToAdd);
        }
        gm.moveToSchool(2,Color.PINK);

        if(!gm.getPlayerByID(2).getEntryStudents().containsKey(Color.YELLOW)
                || gm.getPlayerByID(2).getEntryStudents().get(Color.YELLOW)==0){
            EnumMap<Color, Integer> studentToAdd=new EnumMap<>(Color.class);
            studentToAdd.put(Color.YELLOW,1);
            gm.getPlayerByID(2).addEntryStudents(studentToAdd);
        }
        gm.moveToSchool(2,Color.YELLOW);

        gm.setCurrentPlayer(1);
    }
    @AfterEach
    public void tearDown() {
        cards=null;
        chosenStudents=null;
        entranceStudentsSwap=null;
        entranceStudents=null;
        gm=null;
    }
    @Test
    void testSetChosenStudents() {
        EnumMap<Color, Integer> chosenStudents=new EnumMap<>(Color.class);
        EnumMap<Color, Integer> chosenStudents2=new EnumMap<>(Color.class);
        EnumMap<Color, Integer> chosenStudents3=new EnumMap<>(Color.class);
        EnumMap<Color, Integer> entranceStudents=new EnumMap<>(Color.class);
        chosenStudents.put(Color.BLUE,1);
        chosenStudents2.put(Color.GREEN,1);
        cards[0].setChosenStudents(chosenStudents);
        cards[9].setChosenStudents(chosenStudents2);
        chosenStudents3.put(Color.PINK,1);
        chosenStudents3.put(Color.GREEN,1);
        entranceStudents.put(Color.BLUE,1);
        entranceStudents.put(Color.RED,1);
        cards[5].setChosenStudents(chosenStudents3,entranceStudents);
        cards[8].setChosenStudents(chosenStudents3,entranceStudents);
        assertEquals(cards[0].getChosenStudents().get(),chosenStudents);
        assertEquals(cards[9].getChosenStudents().get(),chosenStudents2);
        assertEquals(cards[5].getChosenStudents().get(),chosenStudents3);
        assertEquals(cards[5].getEntranceStudents().get(),entranceStudents);
        assertEquals(cards[8].getChosenStudents().get(),chosenStudents3);
        assertEquals(cards[8].getEntranceStudents().get(),entranceStudents);
    }

    @Test
    void testUseCard1(){
        chosenStudents.put(Color.BLUE,1);
        int n=gm.getIslandByPosition(1).getStudents().get(Color.BLUE);
        //test effect of card 1
        cards[0].setChosenStudents(chosenStudents);
        cards[0].useCard(1,gm);
        assertEquals(gm.getIslandByPosition(1).getStudents().get(Color.BLUE),n+1);

    }

    @Test
    void testUseCard2(){
        //test effect of card 2
        if(!gm.getCharacterCards().stream().anyMatch(card->card.getAsset().equals("herbalist"))){
            gm.getCharacterCards().remove(2);
            CharacterCard card =new CharacterCard("herbalist.jpg");
            gm.getCharacterCards().add(card);
            gm.getCharactersPositions().put("herbalist.jpg",2);
        }
        Optional<Tower> towerOld=gm.getTowerOnIsland(1);
        gm.getIslandByPosition(1).setNoEntryCard(1);
        cards[1].useCard(1,gm);
        Optional<Tower> towerNew=gm.getTowerOnIsland(1);
        assertEquals(towerOld,towerNew);
        for (Color c: Color.values()) {
            studentOnIsland.put(c,0);
        }
        studentOnIsland.put(Color.BLUE,3);
        gm.moveStudentsToIsland(2,studentOnIsland);
        cards[1].useCard(2,gm);
        assertEquals(gm.getTowerOnIsland(2).get(),gm.getPlayerByID(1).getTower());
    }

    @Test
    void testUseCard3(){
        cards[2].useCard(2,gm);
        assertEquals(gm.isTwoAdditionalSteps(),true);
    }

    @Test
    void testUseCard4(){
        //test effect of card 4
        int noEntry=gm.getIslandByPosition(1).getNoEntryCard();
        cards[3].useCard(1,gm);
        assertEquals(gm.getIslandByPosition(1).getNoEntryCard(),noEntry+1);
    }

    @Test
    void testUseCard5(){
        //test effect of card 5
        cards[4].useCard(2,gm);
        assertEquals(gm.isTowersNotCounted(),true);
    }

    @Test
    void testUseCard6(){
        //test effect of card 6
        chosenStudents.put(Color.PINK,1);
        chosenStudents.put(Color.GREEN,1);
        chosenStudents.put(Color.BLUE,1);
        EnumMap<Color, Integer> entranceStudents=gm.getPlayerByID(1).getEntryStudents();
        System.out.println(entranceStudents);
        int studentNumber=0;
        int key=0;
        for(int i=0; i<5;i++){
            if(entranceStudents.get(Color.values()[i])<=3) {
                key=entranceStudents.get(Color.values()[i]);
                entranceStudentsSwap.put(Color.values()[i], key);
                studentNumber+= key;
            }
            else {
                key=3;
                entranceStudentsSwap.put(Color.values()[i], 3);
                studentNumber+=3;
            }

            while(studentNumber>3){
                entranceStudentsSwap.put(Color.values()[i],key-1);
                key--;
                studentNumber--;
            }
            if(studentNumber==3)
                break;
        }

        System.out.println(chosenStudents);
        System.out.println(entranceStudentsSwap);

        cards[5].setChosenStudents(chosenStudents,entranceStudentsSwap);
        cards[5].useCard(0,gm);
        boolean check=true;
        assertEquals(check,true);
    }

    @Test
    void testUseCard7(){
        //test effect of card 7
        cards[6].useCard(2,gm);
        assertEquals(gm.isTwoAdditionalPoints(),true);
    }

    @Test
    void testUseCard8(){
        //test effect of card 8
        cards[7].setChosenColor(Optional.of(Color.BLUE));
        cards[7].useCard(0,gm);
        assertEquals(gm.getNotCountedColor(),Color.BLUE);

    }

    @Test
    void testUseCard9(){
        entranceStudents=gm.getPlayerByID(1).getEntryStudents();
        EnumMap<Color,Integer>  diningStudents=gm.getPlayerByID(1).getStudents();
        System.out.println(entranceStudents);
        System.out.println(diningStudents);
        int studentNumber=0;
        int studentNumber2=0;
        int i=0;

        while(studentNumber<2) {
            for (Color c : Color.values()) {
                if (studentNumber < 2 && entranceStudents.containsKey(c) && (entranceStudents.get(c)-i) > 0) {
                    entranceStudentsSwap.put(c, 1+i);
                    studentNumber++;
                }
            }
            i++;
        }
        i=0;
        while(studentNumber2<2) {
            for (Color c : Color.values()) {
                if (studentNumber2 < 2 && diningStudents.containsKey(c) && (diningStudents.get(c)-i) > 0) {
                    chosenStudents.put(c, 1+i);
                    studentNumber2++;
                }
            }
            i++;
        }

        System.out.println(entranceStudentsSwap);
        System.out.println(chosenStudents);

        cards[8].setChosenStudents(chosenStudents,entranceStudentsSwap);
        cards[8].useCard(0,gm);
        boolean check=true;
        for(Color c: chosenStudents.keySet()){
            if(gm.getPlayerByID(1).getEntryStudents().get(c)<chosenStudents.get(c)){
                check=false;
            }
        }
        for(Color c:entranceStudentsSwap.keySet()){
            if(gm.getPlayerByID(1).getStudents().get(c)<entranceStudentsSwap.get(c)){
                check=false;
            }
        }
        assertEquals(check,true);

    }

    @Test
    void testUseCard10(){
        //test effect of card 10
        for(Color c: Color.values()){
            chosenStudents.put(c,0);
        }
        Color c=(Color)cards[9].getStudents().get().keySet().toArray()[0];
        int oldNumberStud=gm.getPlayerByID(gm.getCurrentPlayer()).getStudentsOf(c);
        chosenStudents.put(c,1);
        cards[9].setChosenStudents(chosenStudents);
        cards[9].useCard(0,gm);
        assertEquals(gm.getPlayerByID(gm.getCurrentPlayer()).getStudentsOf(c),oldNumberStud+1);
    }

    @Test
    void testUseCard11(){
        //test effect of card 11
        cards[10].setChosenColor(Optional.of(Color.BLUE));
        int numberBlueStud1=gm.getPlayerByID(1).getStudentsOf(Color.BLUE);
        int numberBlueStud2=gm.getPlayerByID(2).getStudentsOf(Color.BLUE);
        if(numberBlueStud1>3)
            numberBlueStud1=3;
        if(numberBlueStud2>3)
            numberBlueStud2=3;
        int key=gm.getNumberOfStudentBag();
        cards[10].useCard(0,gm);
        assertEquals(gm.getNumberOfStudentBag(),key+numberBlueStud2+numberBlueStud1);
    }

    @Test
    void useCard12(){
        //test effect of card 12
        gm.setCurrentPlayer(2);
        gm.getPlayerByID(2).setStudents(Color.RED,gm.getPlayerByID(1).getStudentsOf(Color.RED));
        cards[11].useCard(0,gm);
        gm.moveToSchool(1,Color.RED);
        assertEquals(gm.getProfessors().get(Color.RED).getPlayer(),gm.getPlayerByID(2));
    }

    @Test
    void testIncreaseCost() {
        int n=cards[1].getCost();
        cards[1].increaseCost();
        assertEquals(cards[1].getCost(),n+1);
    }
}