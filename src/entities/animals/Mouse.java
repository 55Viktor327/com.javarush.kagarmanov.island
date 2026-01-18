package entities.animals;

import config.Config;
import entities.Animal;
import entities.Eatable;
import entities.Herbivore;
import entities.Plant;
import entities.enums.AnimalType;
import entities.enums.Gender;
import island.Location;

import java.util.Map;

public class Mouse extends Herbivore {
    public final int probabilityOfEating;

    public Mouse(AnimalType type, Gender gender, Location location) {
        super(type, gender, location);
        probabilityOfEating = Config.PROBABILITY_OF_EATING.get(this.getType()).get(AnimalType.CATERPILLAR);
    }

    @Override
    public void eat(Eatable food){
        if(food instanceof Plant){
            Plant meal = (Plant) food;
            double desiredAmount = this.getType().getFoodRequired();
            double availableAmount = ((Plant) food).getWeight();
            double actualAmount = Math.min(desiredAmount, availableAmount);
            this.gainWeight(actualAmount);
            ((Plant) food).decrementWeight(actualAmount);
        } else if(food instanceof Animal) {
            Animal prey = (Animal) food;

            if (prey.getType() == AnimalType.CATERPILLAR) {
                if ((Math.random() * 100) <= probabilityOfEating) {
                    this.gainWeight(prey.getCurrentWeight());
                    prey.die();
                    Location location = this.getLocation();
                    if (location != null) {
                        location.tryRemoveAnimal(prey);
                    }
                }
            }
        }
    }
}
