package it.polimi.ingsw.Model;

/**
 * Professor class
 * @author Angelo Zagami
 */
public class Professor {

    private final Color color;
    private Player player;

    /**
     *
     * @param color Color of professor
     */
    public Professor(Color color){
        this.color = color;
    }

    /**
     * Moves the teacher into the player's school
     * @param player Indicates the player who owns the school where the teacher moves
     */
    public void goToSchool(Player player){
        this.player = player;
    }

    public player getPlayer(){
        return player;
    }

}
