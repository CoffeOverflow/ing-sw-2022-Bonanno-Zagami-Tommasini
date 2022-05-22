package it.polimi.ingsw.Model;

public class Conquest {
    private Tower conqueror;

    private Integer conqueredIsland;
    private Integer mergedIsland1;
    private Integer mergedIsland2;

    public Conquest(Tower conqueror, Integer conqueredIsland1, Integer mergedIsland1, Integer mergedIsland2) {
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
