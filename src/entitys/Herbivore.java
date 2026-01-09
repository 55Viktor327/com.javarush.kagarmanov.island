package entitys;

import enums.AnimalType;
import enums.Gender;
import island.Location;

import java.util.Optional;

public class Herbivore extends Animal implements Eatable{
    public Herbivore(AnimalType type, Gender gender, Location location) {
        super(type, gender, location);
    }

    @Override
    protected void eat(Eatable food) {

    }

    @Override
    protected void move(Location location) {

    }

    @Override
    protected Optional<Animal> reproduce(Animal partner) {
        return Optional.empty();
    }
}
