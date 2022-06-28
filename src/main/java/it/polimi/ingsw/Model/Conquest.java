package it.polimi.ingsw.Model;

/** Conquest class
 * @author Federica Tommasini
 */
public class Conquest {
    private Tower conqueror;

    private Integer conqueredIsland;
    private Integer mergedIsland1;
    private Integer mergedIsland2;

    /**
     * create a Conquest object that contains the information regarding the conquest of an island
     * @param conqueror player who placed the tower
     * @param conqueredIsland island conquered
     * @param mergedIsland1 possible near island to merge, null if not present
     * @param mergedIsland2 other possible near island to merge, null if not present
     */
    public Conquest(Tower conqueror, Integer conqueredIsland, Integer mergedIsland1, Integer mergedIsland2) {
        this.conqueror = conqueror;
        this.conqueredIsland = conqueredIsland;
        this.mergedIsland1 = mergedIsland1;
        this.mergedIsland2 = mergedIsland2;
    }

    public Tower getConqueror() {
        return conqueror;
    }

    public Integer getConqueredIsland() {
        return conqueredIsland;
    }

    public Integer getMergedIsland1() {
        return mergedIsland1;
    }
    public Integer getMergedIsland2() {
        return mergedIsland2;
    }
}
