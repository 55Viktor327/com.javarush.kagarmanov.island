package island;

import config.Config;
import entities.Animal;
import entities.Plant;
import entities.enums.AnimalType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Location {
    private final int coordinateX;
    private final int coordinateY;
    private Map<AnimalType, List<Animal>> animals;
    private List<Plant> plants;

    public Location(int coordinateX, int coordinateY){
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        animals = new ConcurrentHashMap<>();
        plants = new CopyOnWriteArrayList<>();
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public Map<AnimalType, List<Animal>> getAnimals() {
        return animals;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public void growthOfPlants() {
        double maxPlantMass = Config.MAX_WEIGHT_OF_PLANT_IN_THE_CELL_IN_KG;
        double growthRate = Config.PLANT_GROWTH_RATE;

        // 1. Растут существующие растения
        for (Plant plant : plants) {
            double currentWeight = plant.getWeight();
            double growth = currentWeight * growthRate;

            // Проверяем, не превысим ли общую массу
            double totalMass = getTotalPlantMass();
            if (totalMass + growth <= maxPlantMass) {
                plant.incrementWeight(growth);
            }
        }

        // 2. Добавляем новые растения, если есть место
        double totalMass = getTotalPlantMass();
        double availableMass = maxPlantMass - totalMass;

        if (availableMass > Config.WEIGHT_OF_PLANT_IN_KG) {
            Plant newPlant = new Plant(this);
            plants.add(newPlant);
        }

        // 3. Удаляем "мёртвые" растения
        plants.removeIf(plant -> plant.getWeight() <= 0.001);
    }

    private double getTotalPlantMass() {
        return plants.stream().mapToDouble(Plant::getWeight).sum();
    }
    public boolean tryAddAnimal(Animal animal) {
        AnimalType type = animal.getType();
        int capacity = Config.MAX_POPULATION_ANIMAL_IN_THE_CELL.get(type);
        return animals.compute(type, (key, list) -> {
            if (list == null) {
                list = new CopyOnWriteArrayList<>();
            }

            if (list.size() < capacity) {
                list.add(animal);
            }
            return list;
        }).contains(animal); // Проверяем, добавился ли
    }

    public boolean tryRemoveAnimal(Animal animal) {
        AtomicBoolean removed = new AtomicBoolean(false);

        animals.computeIfPresent(animal.getType(), (type, list) -> {
            removed.set(list.remove(animal));
            return list.isEmpty() ? null : list; // Удаляем из мапы, если список пуст
        });

        return removed.get();
    }
}
