package it.polimi.ingsw.Model;

import java.util.Optional;

/**
 * Professor class
 * @author Angelo Zagami
 */
public class Professor {

    private final Color color;
    private Optional<Player> player;

    /**
     * Class constructor
     * @param color Color of professor
     */
    public Professor(Color color){
        this.color = color;
        this.player = Optional.empty();
    }

    /**
     * Moves the teacher into the player's school
     * @param player Indicates the player who owns the school where the teacher moves
     */
    public void goToSchool(Player player){
        this.player.ifPresent(value -> value.removeProfessor(color));
        this.player = Optional.of(player);
        this.player.get().addProfessor(color);
    }

    /**
     * Get the player that controls the professor
     * @return Optional<Player> The player if there is one, null otherwise
     */
    public Player getPlayer(){
        return player.orElse(null);
    }

    /***
     * Get the color of the professor
     * @return Professor's color
     */
    public Color getColor(){ return color; }

}