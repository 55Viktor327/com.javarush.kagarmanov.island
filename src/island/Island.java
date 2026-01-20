package island;

import config.Config;
import entities.Animal;
import entities.Plant;
import entities.enums.AnimalType;
import factory.AnimalFactory;
import factory.PlantFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Island {
    private static Island island = getIsland();
    private final Location[][] cells;
    private final List<Location> allLocations;
    private final Map<AnimalType, AtomicInteger> currentPopulation;

    private Island(){
        cells = new Location[Config.WIDTH_ISLAND][Config.LENGTH_ISLAND];
        allLocations = Collections.synchronizedList(new ArrayList<>());

        for (int x = 0; x < Config.WIDTH_ISLAND; x++) {
            for (int y = 0; y < Config.LENGTH_ISLAND; y++) {
                Location location = new Location(x,y);
                cells[x][y] = location;
                allLocations.add(location);
            }
        }

        currentPopulation = new ConcurrentHashMap<>();
        for(AnimalType type : AnimalType.values()){
            currentPopulation.put(type, new AtomicInteger(0));
        }
    }

    public static Island getIsland(){
        if (island == null) {
            synchronized (Island.class) {
                if (island == null) {
                    island = new Island();
                }
            }
        }
        return island;
    }

    public int getWidthIsland(){
        return cells.length;
    }

    public int getLengthIsland(){
        return cells[0].length;
    }

    public Location getLocation(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return cells[x][y];
        }
        return null;
    }

     public boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < Config.WIDTH_ISLAND &&
                y >= 0 && y < Config.LENGTH_ISLAND;
    }

    public List<Location> getAllLocations(){
        return allLocations;
    }

    public List<Location> getAdjacentLocations(Location location) {
        List<Location> adjacent = new ArrayList<>();
        int x = location.getCoordinateX();
        int y = location.getCoordinateY();

        // Проверяем все 4 направления
        checkAndAddLocation(x, y - 1, adjacent); // UP
        checkAndAddLocation(x, y + 1, adjacent); // DOWN
        checkAndAddLocation(x - 1, y, adjacent); // LEFT
        checkAndAddLocation(x + 1, y, adjacent); // RIGHT

        return adjacent;
    }

    private void checkAndAddLocation(int x, int y, List<Location> list) {
        if (isValidCoordinate(x, y)) {
            list.add(cells[x][y]);
        }
    }

    public void incrementPopulation(AnimalType type) {
        currentPopulation.get(type).incrementAndGet();
    }

    public void decrementPopulation(AnimalType type) {
        currentPopulation.get(type).decrementAndGet();
    }

    public Map<AnimalType, Integer> getCurrentPopulationOfAnimals() {
        Map<AnimalType, Integer> snapshot = new HashMap<>();
        for (Map.Entry<AnimalType, AtomicInteger> entry : currentPopulation.entrySet()) {
            snapshot.put(entry.getKey(), entry.getValue().get());
        }
        return snapshot;
    }

    public void initialize(AnimalFactory animalFactory, PlantFactory plantFactory) {
        for (Location location : allLocations) {
            initializePlants(location, plantFactory);
            initializeAnimals(location, animalFactory);
        }
        printInitialStatistics();
    }

    private void initializePlants(Location location, PlantFactory plantFactory) {
        int maxPlants = Config.MAX_NUMBER_OF_PLANTS_IN_THE_CELL;
        int plantCount = ThreadLocalRandom.current().nextInt(0, (int)(maxPlants * 0.4));

        for (int i = 0; i < plantCount; i++) {
            Plant plant = plantFactory.createPlant(location);
            location.getPlants().add(plant);
        }
    }

    private void initializeAnimals(Location location, AnimalFactory animalFactory) {
        for (AnimalType type : AnimalType.values()) {
            int maxOnCell = Config.MAX_POPULATION_ANIMAL_IN_THE_CELL.get(type);
            double count = ThreadLocalRandom.current().nextInt(0,(int)(maxOnCell * 0.7));

            for (int i = 0; i < count; i++) {
                Animal animal = animalFactory.createAnimal(type, location);
                if (location.tryAddAnimal(animal)) {
                    incrementPopulation(type);
                }
            }
        }
    }

    private void printInitialStatistics() {
        System.out.println("=== Инициализация острова завершена ===");
        System.out.println("Размер: " + Config.WIDTH_ISLAND + "x" + Config.LENGTH_ISLAND);
        System.out.println("Локаций: " + allLocations.size());

        Map<AnimalType, Integer> population = getCurrentPopulationOfAnimals();
        int totalAnimals = population.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("Всего животных: " + totalAnimals);
    }
}
