package island;

import config.Config;
import enums.AnimalType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Island {
    private static Island ISLAND = new Island();
    private static Location[][] locations;
    public Map<AnimalType, Integer> currentPopulationOfAnimals;

    private Island(){
        locations = new Location[Config.WIDTH_ISLAND][Config.LENGTH_ISLAND];
        for (int i = 0; i < Config.WIDTH_ISLAND; i++) {
            for (int j = 0; j < Config.LENGTH_ISLAND; j++) {
                locations[i][j] = new Location(i,j);
            }
        }

        currentPopulationOfAnimals = new ConcurrentHashMap<>();
        for(AnimalType type : AnimalType.values()){
            currentPopulationOfAnimals.put(type, 0);
        }
    }

    public static Island getIsland(){
        return ISLAND;
    }

    public Map<AnimalType, Integer> getCurrentPopulationOfAnimals() {
        return currentPopulationOfAnimals;
    }
}
