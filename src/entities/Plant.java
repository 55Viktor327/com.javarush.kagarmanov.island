package entities;

import config.Config;
import island.Location;

public class Plant implements Eatable {
    private double weight = Config.WEIGHT_OF_PLANT_IN_KG;
    private final Location location;

    public Plant(Location location){
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public double getWeight() {
        return weight;
    }

    public void incrementWeight(double amount){
        this.weight += amount;
    }

    public void decrementWeight(double amount){
        this.weight = Math.max(0, weight - amount);
    }
}
