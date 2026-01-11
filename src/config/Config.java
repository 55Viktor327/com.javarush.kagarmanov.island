package config;

import enums.AnimalType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

public class Config {
// 1. РАЗМЕР ОСТРОВА
    public static final int WIDTH_ISLAND  = 100;
    public static final int LENGTH_ISLAND = 20;
// 2. ВЕРОЯТНОСТИ ПОЕДАНИЯ
    public static final Map<AnimalType, Integer> PROBABILITY_OF_EATING_BY_WOLF = new HashMap<>();
    public static final Map<AnimalType, Integer> PROBABILITY_OF_EATING_BY_FOX = new HashMap<>();
    public static final Map<AnimalType, Integer> PROBABILITY_OF_EATING_BY_EAGLE = new HashMap<>();
    public static final Map<AnimalType, Integer> PROBABILITY_OF_EATING_BY_BOA = new HashMap<>();
    public static final Map<AnimalType, Integer> PROBABILITY_OF_EATING_BY_BEAR = new HashMap<>();
    public static final Map<AnimalType, Integer> PROBABILITY_OF_EATING_BY_HOG = new HashMap<>();
    public static final Map<AnimalType, Integer> PROBABILITY_OF_EATING_BY_DUCK = new HashMap<>();
    public static final Map<AnimalType, Integer> PROBABILITY_OF_EATING_BY_MOUSE = new HashMap<>();
    public static final Map<AnimalType, Map<AnimalType, Integer>> PROBABILITY_OF_EATING = Map.of(
            AnimalType.WOLF, PROBABILITY_OF_EATING_BY_WOLF,
            AnimalType.FOX, PROBABILITY_OF_EATING_BY_FOX,
            AnimalType.EAGLE, PROBABILITY_OF_EATING_BY_EAGLE,
            AnimalType.BOA, PROBABILITY_OF_EATING_BY_BOA,
            AnimalType.BEAR, PROBABILITY_OF_EATING_BY_BEAR,
            AnimalType.HOG, PROBABILITY_OF_EATING_BY_HOG,
            AnimalType.DUCK, PROBABILITY_OF_EATING_BY_DUCK,
            AnimalType.MOUSE, PROBABILITY_OF_EATING_BY_MOUSE);

// 3. ХАРАКТЕРИСТИКИ ЖИВОТНЫХ
    public static final Map<AnimalType, Double> WEIGHT_OF_ANIMAL = new HashMap<>();
    public static final Map<AnimalType, Integer> MAX_POPULATION_ANIMAL_IN_THE_CELL = new HashMap<>();
    public static final Map<AnimalType, Integer> MOVEMENT_SPEED_ANIMALS = new HashMap<>();
    public static final Map<AnimalType, Double> AMOUNT_OF_FOOD_NEEDED_FOR_FULL_SATURATION = new HashMap<>();
    public static final Map<AnimalType, Integer> MAX_AGE_ANIMALS = new HashMap<>();
    public static final int START_HEALTH = 100;
    public static final int SATIETY = 70;
    public static final int REPRODUCTION_COOLDOWN = 0;
    public static final int AGE = 0;
    public static final boolean IS_ALIVE = true;

// 4. ХАРАКТЕРИСТИКИ РАСТЕНИЙ
    public static final int WEIGHT_OF_PLANT = 1;
    public static final int MAX_NUMBER_OF_PLANTS_IN_THE_CELL = 200;

// 5. МИНИМУМ ТРАВОЯДНЫХ, МИНИМУМ ХИЩНИКОВ
    public static final int MIN_PREDATORS_TYPE = 5;
    public static final int MIN_HERBIVORE_TYPE = 10;

    static{
        for(AnimalType type : AnimalType.values()){
            WEIGHT_OF_ANIMAL.put(type, type.getWeight());
        }

        for(AnimalType type : AnimalType.values()){
            MAX_POPULATION_ANIMAL_IN_THE_CELL.put(type, type.getMaxPopulation());
        }

        for(AnimalType type : AnimalType.values()){
            MOVEMENT_SPEED_ANIMALS.put(type, type.getMaxPopulation());
        }

        for(AnimalType type : AnimalType.values()){
            AMOUNT_OF_FOOD_NEEDED_FOR_FULL_SATURATION.put(type, type.getFoodRequired());
        }

        for(AnimalType type : AnimalType.values()){
            MAX_AGE_ANIMALS.put(type, type.getMaxAge());
        }

        PROBABILITY_OF_EATING_BY_WOLF.put(AnimalType.HORSE, 10);
        PROBABILITY_OF_EATING_BY_WOLF.put(AnimalType.DEER, 15);
        PROBABILITY_OF_EATING_BY_WOLF.put(AnimalType.RABBIT, 60);
        PROBABILITY_OF_EATING_BY_WOLF.put(AnimalType.MOUSE, 80);
        PROBABILITY_OF_EATING_BY_WOLF.put(AnimalType.GOAT, 60);
        PROBABILITY_OF_EATING_BY_WOLF.put(AnimalType.SHEEP, 70);
        PROBABILITY_OF_EATING_BY_WOLF.put(AnimalType.HOG, 15);
        PROBABILITY_OF_EATING_BY_WOLF.put(AnimalType.BUFFALO, 10);
        PROBABILITY_OF_EATING_BY_WOLF.put(AnimalType.DUCK, 40);
        PROBABILITY_OF_EATING_BY_BOA.put(AnimalType.FOX, 15);
        PROBABILITY_OF_EATING_BY_BOA.put(AnimalType.RABBIT, 20);
        PROBABILITY_OF_EATING_BY_BOA.put(AnimalType.MOUSE, 40);
        PROBABILITY_OF_EATING_BY_BOA.put(AnimalType.DUCK, 10);
        PROBABILITY_OF_EATING_BY_FOX.put(AnimalType.RABBIT, 70);
        PROBABILITY_OF_EATING_BY_FOX.put(AnimalType.MOUSE, 90);
        PROBABILITY_OF_EATING_BY_FOX.put(AnimalType.DUCK, 60);
        PROBABILITY_OF_EATING_BY_FOX.put(AnimalType.CATERPILLAR, 40);
        PROBABILITY_OF_EATING_BY_BEAR.put(AnimalType.BOA, 80);
        PROBABILITY_OF_EATING_BY_BEAR.put(AnimalType.HORSE, 40);
        PROBABILITY_OF_EATING_BY_BEAR.put(AnimalType.DEER, 80);
        PROBABILITY_OF_EATING_BY_BEAR.put(AnimalType.RABBIT, 80);
        PROBABILITY_OF_EATING_BY_BEAR.put(AnimalType.MOUSE, 90);
        PROBABILITY_OF_EATING_BY_BEAR.put(AnimalType.GOAT, 70);
        PROBABILITY_OF_EATING_BY_BEAR.put(AnimalType.SHEEP, 70);
        PROBABILITY_OF_EATING_BY_BEAR.put(AnimalType.HOG, 50);
        PROBABILITY_OF_EATING_BY_BEAR.put(AnimalType.BUFFALO, 20);
        PROBABILITY_OF_EATING_BY_BEAR.put(AnimalType.DUCK, 20);
        PROBABILITY_OF_EATING_BY_EAGLE.put(AnimalType.FOX, 10);
        PROBABILITY_OF_EATING_BY_EAGLE.put(AnimalType.RABBIT, 90);
        PROBABILITY_OF_EATING_BY_EAGLE.put(AnimalType.MOUSE, 90);
        PROBABILITY_OF_EATING_BY_EAGLE.put(AnimalType.DUCK, 80);
        PROBABILITY_OF_EATING_BY_MOUSE.put(AnimalType.CATERPILLAR, 90);
        PROBABILITY_OF_EATING_BY_HOG.put(AnimalType.MOUSE, 50);
        PROBABILITY_OF_EATING_BY_HOG.put(AnimalType.CATERPILLAR, 90);
        PROBABILITY_OF_EATING_BY_DUCK.put(AnimalType.CATERPILLAR, 90);
    }
}
