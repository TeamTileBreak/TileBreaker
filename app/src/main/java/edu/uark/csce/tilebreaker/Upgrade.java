package edu.uark.csce.tilebreaker;

/**
 * Created by bango on 12/8/2014.
 */
public class Upgrade {

    private String name;
    private String displayName;
    private int cost;

    public Upgrade(String name, String displayName, int cost) {
        this.name = name;
        this.displayName = displayName;
        this.cost = cost;
    }

    public String getName() {
        return this.name;
    }
    public String getDisplayName() {
        return this.displayName;
    }
    public int getCost() {
        return this.cost;
    }
    public String toString() {
        return this.name;
    }
}
