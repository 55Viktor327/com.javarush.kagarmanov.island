package entities.animals;

import entities.Predator;
import entities.enums.AnimalType;
import entities.enums.Gender;
import island.Location;

public class Fox extends Predator {
    public Fox(AnimalType type, Gender gender, Location location) {
        super(type, gender, location);
    }
}
