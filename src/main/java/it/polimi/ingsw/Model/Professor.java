package it.polimi.ingsw.Model;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Professor class
 * @author Angelo Zagami
 */
public class Professor {

    private final Color color;
    private Optional<Player> player;

    /**
     *
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
     *
     * @return Optional<Player>
     * @throws NoSuchElementException
     */
    public Player getPlayer() throws NoSuchElementException {
        if(player.isPresent())
            return player.get();
        else
            return null;
           // throw new NoSuchElementException(); SISTEMARE QUAS
    }

    public Color getColor(){ return color; }

}