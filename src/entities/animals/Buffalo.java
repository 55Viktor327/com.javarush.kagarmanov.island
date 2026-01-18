package entities.animals;

import entities.Herbivore;
import entities.enums.AnimalType;
import entities.enums.Gender;
import island.Location;

public class Buffalo extends Herbivore {
    public Buffalo(AnimalType type, Gender gender, Location location) {
        super(type, gender, location);
    }
}
