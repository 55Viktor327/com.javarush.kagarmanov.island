package entitys;

import config.Config;
import island.Location;

public class Plant implements Eatable {
    private final int WEIGHT = Config.WEIGHT_OF_PLANT;
    private Location location;

    public Plant(Location location){
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
