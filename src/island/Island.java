package island;

import config.Config;
import enums.AnimalType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Island {
    private static Island island = getIsland();
    private static Location[][] locations;
    public Map<AnimalType, Integer> currentPopulationOfAnimals;
    public List<Location> locationList = Collections.synchronizedList(new ArrayList<>());

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
        if(island == null){
            island = new Island();
        }
        return island;
    }

    public Map<AnimalType, Integer> getCurrentPopulationOfAnimals() {
        return currentPopulationOfAnimals;
    }
}
