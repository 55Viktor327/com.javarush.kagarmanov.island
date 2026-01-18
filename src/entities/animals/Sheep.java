package entities.animals;

import entities.Herbivore;
import entities.enums.AnimalType;
import entities.enums.Gender;
import island.Location;

public class Sheep extends Herbivore {
    public Sheep(AnimalType type, Gender gender, Location location) {
        super(type, gender, location);
    }
}
