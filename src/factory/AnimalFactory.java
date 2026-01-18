package factory;

import entities.Animal;
import entities.animals.*;
import entities.enums.AnimalType;
import entities.enums.Gender;
import island.Location;

public class AnimalFactory {
    public static final AnimalFactory INSTANCE = new AnimalFactory();

    public AnimalFactory() {}

    public static Animal createAnimal(AnimalType type, Location loc){
        return INSTANCE.createAnimalInstance(type, loc);
    }

    private Animal createAnimalInstance(AnimalType type, Location location){
        Gender gender = Gender.random();

        switch (type){
            case WOLF -> { return new Wolf(type, gender, location); }
            case BOA -> { return new Boa(type, gender, location); }
            case FOX -> { return new Fox(type, gender, location);}
            case BEAR -> { return new Bear(type, gender, location); }
            case EAGLE -> { return new Eagle(type, gender, location); }
            case RABBIT -> { return new Rabbit(type, gender, location); }
            case BOAR -> { return new Boar(type, gender, location); }
            case DEER -> { return new Deer(type, gender, location); }
            case HORSE -> { return new Horse(type, gender, location); }
            case GOAT -> { return new Goat(type, gender, location); }
            case SHEEP -> { return new Sheep(type, gender, location); }
            case DUCK -> { return new Duck(type, gender, location); }
            case MOUSE -> { return new Mouse(type, gender, location); }
            case BUFFALO -> { return new Buffalo(type, gender, location); }
            case CATERPILLAR -> { return new Caterpillar(type, gender, location); }
            default -> { throw new IllegalArgumentException("Неизвестный тип животного" + type); }
        }
    }
}
