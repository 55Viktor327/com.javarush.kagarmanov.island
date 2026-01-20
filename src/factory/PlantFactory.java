package factory;

import entities.Plant;
import island.Location;

public class PlantFactory {
    private static final PlantFactory INSTANCE = new PlantFactory();

    public PlantFactory(){

    }

    public static PlantFactory getInstance(){
        return INSTANCE;
    }

    public static Plant createPlant(Location loc){
        return INSTANCE.createPlantInstatnce(loc);
    }

    private Plant createPlantInstatnce(Location loc){
        return new Plant(loc);
    }
}
