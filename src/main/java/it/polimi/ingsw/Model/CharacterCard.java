package it.polimi.ingsw.Model;

import java.util.EnumMap;
import java.util.Optional;

public class CharacterCard {
    int cost;
    String asset;
    Effect effect;
    Optional<EnumMap<Color, Integer>> students;
    Optional<EnumMap<Color, Integer>> chosenStudents;

    //costruttore per le carte personaggio che hanno degli studenti sopra
    public CharacterCard(int cost, String asset, EnumMap<Color, Integer> students) {

        this.cost = cost;
        this.asset = asset;

        switch (asset) {
            case "CarteTOT_front": //carta che ha 4 studenti e il giocatore ne sceglie uno da mettere su un'isola
                if (students!=null && students.size() == 4) {
                    this.students.of(students);
                }else{
                    throw new IllegalArgumentException("the card must contain four students and the chosen student must be one");
                }
                effect = new Effect1();
                break;
            case "CarteTOT_front6": //carta che ha 6 studenti, il giocatore ne può prendere al max 3 e scambiarli con 3 presenti nel suo ingresso
                if (students!=null && students.size() == 6){
                    this.students.of(students);
                }
                else {
                    throw new IllegalArgumentException("the card must contain six students and the chosen students must be at most three");
                }
                effect = new Effect6();
                break;
            case "CarteTOT_front10": //carta che ha 4 studenti, il giocatore ne può scegliere uno da mettere nella sua sala
                if (students!=null && students.size() == 4){
                    this.students.of(students);
                }
                else {
                    throw new IllegalArgumentException("the card must contain four students and the chosen student must be one");
                }
                effect = new Effect10();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + asset);
        }
    }


    public int getCost(){
        return cost;
    }

    public String getAsset(){
        return asset;
    }

    public void setStudents(EnumMap<Color, Integer> students) {
        this.students.of(students);
    }

    public void setChosenStudents(EnumMap<Color, Integer> chosenStudents) {
        if( (this.asset.equals("CarteTOT_front1") && chosenStudents.size()==1) ||
            (this.asset.equals("CarteTOT_front6") && chosenStudents.size()<=3) ||
            (this.asset.equals("CarteTOT_front10") && chosenStudents.size()==1)
        )
        this.chosenStudents.of(chosenStudents);
        else{
            throw new IllegalStateException("Unexpected number of chosen students: " + chosenStudents.size());
        }
    }

    public void useCard(Player player, Island island){
        effect.effect(player,island, this);
        if(chosenStudents.isPresent()){
            //TODO qua devo togliere gli studenti scelti dalla carta
            chosenStudents.empty();
            //TODO trovare un modo per estrarne altri dalla bag (probabilmente va spostato nel metodo useCharacterCard del model
        }
    }



}
