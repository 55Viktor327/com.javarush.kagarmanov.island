package island;

import config.Config;
import entities.Animal;
import entities.Plant;
import entities.enums.AnimalType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Location {
    private final int coordinateX;
    private final int coordinateY;
    private final ConcurrentMap<AnimalType, CopyOnWriteArrayList<Animal>> animals;
    private final CopyOnWriteArrayList<Plant> plants;
    private final AtomicInteger animalCount = new AtomicInteger(0);
    private final Object lock = new Object();

    public Location(int coordinateX, int coordinateY) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.animals = new ConcurrentHashMap<>();
        this.plants = new CopyOnWriteArrayList<>();
    }

    public int getCoordinateX() { return coordinateX; }

    public int getCoordinateY() { return coordinateY; }

    public List<Plant> getPlants() {
        return new ArrayList<>(plants);
    }

    public ConcurrentMap<AnimalType, CopyOnWriteArrayList<Animal>> getAnimalsMap() {
        return animals;
    }

    public boolean tryAddAnimal(Animal animal) {
        AnimalType type = animal.getType();
        int capacity = Config.MAX_POPULATION_ANIMAL_IN_THE_CELL.get(type);
        boolean[] added = new boolean[1];

        animals.compute(type, (key, list) -> {
            if (list == null) {
                list = new CopyOnWriteArrayList<>();
            }

            if (list.size() < capacity) {
                list.add(animal);
                added[0] = true;
                animalCount.incrementAndGet();
            }
            return list;
        });

        return added[0];
    }

    public boolean tryRemoveAnimal(Animal animal) {
        AtomicBoolean removed = new AtomicBoolean(false);
        animals.computeIfPresent(animal.getType(), (type, list) -> {
            if (list.remove(animal)) {
                removed.set(true);
                animalCount.decrementAndGet();
                return list.isEmpty() ? null : list;
            }
            return list;
        });
        return removed.get();
    }

    public boolean tryMoveAnimalTo(Animal animal, Location target) {
        if (this == target) {
            return true;
        }

        Location first, second;
        if (System.identityHashCode(this) < System.identityHashCode(target)) {
            first = this;
            second = target;
        } else {
            first = target;
            second = this;
        }

        synchronized (first) {
            synchronized (second) {
                if (target.tryAddAnimal(animal)) {
                    boolean removed = this.tryRemoveAnimal(animal);
                    if (removed) {
                        animal.setLocation(target);
                        return true;
                    } else {
                        target.tryRemoveAnimal(animal);
                    }
                }
                return false;
            }
        }
    }

    public List<Animal> getAllAnimals() {
        return animals.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Animal> getAnimalsByTypeSafe(AnimalType type) {
        CopyOnWriteArrayList<Animal> list = animals.get(type);
        return list != null ? new ArrayList<>(list) : new ArrayList<>();
    }

    public List<Animal> findAnimalsByTypes(Set<AnimalType> types) {
        List<Animal> result = new ArrayList<>();
        for (AnimalType type : types) {
            CopyOnWriteArrayList<Animal> list = animals.get(type);
            if (list != null && !list.isEmpty()) {
                List<Animal> copy = new ArrayList<>(list);
                copy.stream()
                        .filter(Animal::isAlive)
                        .forEach(result::add);
            }
        }
        return result;
    }

    public Optional<Plant> findPlantForEating() {
        return plants.stream()
                .filter(plant -> plant.getWeight() > 0)
                .findFirst();
    }

    public Optional<Animal> findPartnerFor(Animal animal) {
        CopyOnWriteArrayList<Animal> animalList = animals.get(animal.getType());
        if (animalList == null || animalList.isEmpty()) {
            return Optional.empty();
        }

        List<Animal> sameType = new ArrayList<>(animalList);
        return sameType.stream()
                .filter(a -> a != animal)
                .filter(Animal::isAlive)
                .filter(a -> a.getGender() != animal.getGender())
                .filter(a -> a.canReproduce() && animal.canReproduce())
                .findFirst();
    }

    public boolean removePlant(Plant plant) {
        return plants.remove(plant);
    }

    public void growthOfPlants() {
        double maxPlantMass = Config.MAX_WEIGHT_OF_PLANT_IN_THE_CELL_IN_KG;
        double growthRate = Config.PLANT_GROWTH_RATE;
        double currentTotalMass = getTotalPlantMass();
        double availableGrowth = maxPlantMass - currentTotalMass;
        if (availableGrowth <= 0) {
            return;
        }

        if (!plants.isEmpty()) {
            double growthPerPlant = availableGrowth * growthRate / plants.size();
            for (Plant plant : plants) {
                plant.incrementWeight(growthPerPlant);
            }
        }

        currentTotalMass = getTotalPlantMass();
        availableGrowth = maxPlantMass - currentTotalMass;

        if (availableGrowth > Config.WEIGHT_OF_PLANT_IN_KG &&
                plants.size() < Config.MAX_NUMBER_OF_PLANTS_IN_THE_CELL) {
            plants.add(new Plant(this));
        }

        plants.removeIf(plant -> plant.getWeight() <= 0.001);
    }

    private double getTotalPlantMass() {
        double total = 0.0;
        for (Plant plant : plants) {
            total += plant.getWeight();
        }
        return total;
    }
}
