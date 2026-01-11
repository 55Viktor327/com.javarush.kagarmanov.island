package island;

import config.Config;
import entitys.Animal;
import entitys.Plant;
import enums.AnimalType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location {
    private final int X;
    private final int Y;
    private Map<AnimalType, List<Animal>> animals = new HashMap<>();
    private List<Plant> plants = new ArrayList<>();


    public Location(int x, int y){
        this.X = x;
        this.Y = y;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public Map<AnimalType, List<Animal>> getAnimals() {
        return animals;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    private void growthOfPlants(){
        if(plants.size() != Config.MAX_NUMBER_OF_PLANTS_IN_THE_CELL){
            plants.add(new Plant(this));
        }
    }

    private void addAnimal(Animal animal){
        List<Animal> tempList = animals.get(animal.getType());

        if(tempList.size() < Config.MAX_POPULATION_ANIMAL_IN_THE_CELL.get(animal.getType())){
            tempList.add(animal);
        }
        animals.put(animal.getType(), tempList);
    }
}
