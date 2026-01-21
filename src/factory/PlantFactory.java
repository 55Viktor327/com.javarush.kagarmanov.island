package factory;

import entities.Plant;
import island.Location;

public class PlantFactory {
    private static final PlantFactory INSTANCE = new PlantFactory();

    public PlantFactory(){

    }

    public static Plant createPlant(Location loc){
        return INSTANCE.createPlantInstance(loc);
    }

    private Plant createPlantInstance(Location loc){
        return new Plant(loc);
    }
}
