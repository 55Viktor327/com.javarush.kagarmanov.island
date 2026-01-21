package entities.animals;

import config.Config;
import entities.Animal;
import entities.Herbivore;
import entities.Plant;
import entities.enums.AnimalType;
import entities.enums.Gender;
import island.Location;
import simulation.StepContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Duck extends Herbivore {
    public final int caterpillarEatingProbability;

    public Duck(AnimalType type, Gender gender, Location location) {
        super(type, gender, location);
        caterpillarEatingProbability = Config.PROBABILITY_OF_EATING.get(this.getType()).get(AnimalType.CATERPILLAR);
    }


    @Override
    public void eat(StepContext context){
        Location location = this.getLocation();
        if (location == null) return;

        boolean hasEaten = tryEatPlant(location);

        if (!hasEaten) {
            hasEaten = tryEatCaterpillar(location, context);
        }

        if (!hasEaten) {
            this.loseWeight(Config.BASE_HUNGER_LOSS, context);
        }
    }

    private boolean tryEatPlant(Location location) {
        Optional<Plant> plantOpt = location.findPlantForEating();

        if (plantOpt.isPresent()) {
            Plant plant = plantOpt.get();
            double desiredAmount = this.getType().getFoodRequired();
            double availableAmount = plant.getWeight();
            double eatenAmount = Math.min(desiredAmount, availableAmount);
            this.gainWeight(eatenAmount);
            plant.decrementWeight(eatenAmount);
            if (plant.getWeight() <= 0) {
                location.removePlant(plant);
            }
            return true;
        }
        return false;
    }

    private boolean tryEatCaterpillar(Location location, StepContext context) {
        Set<AnimalType> caterpillarType = Collections.singleton(AnimalType.CATERPILLAR);
        List<Animal> caterpillars = location.findAnimalsByTypes(caterpillarType);

        if (caterpillars.isEmpty()) return false;

        Animal caterpillar = caterpillars.get(0);
        if (ThreadLocalRandom.current().nextInt(100) < caterpillarEatingProbability) {
            this.gainWeight(caterpillar.getCurrentWeight());
            context.markAnimalForRemoval(caterpillar);
            return true;
        }
        return false;
    }
}
