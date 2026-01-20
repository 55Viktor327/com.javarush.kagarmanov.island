package entities.animals;

import config.Config;
import entities.Animal;
import entities.Herbivore;
import entities.Plant;
import entities.enums.AnimalType;
import entities.enums.Gender;
import island.Location;
import simulation.StepContext;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Boar extends Herbivore {
    public final Map<AnimalType, Integer> probabilityOfEating;

    public Boar(AnimalType type, Gender gender, Location location) {
        super(type, gender, location);
        probabilityOfEating = Config.PROBABILITY_OF_EATING.get(this.getType());
    }
    @Override
    public void eat(StepContext context){
        Location location = this.getLocation();
        if (location == null) return;
        boolean hasEatenPlant = tryEatPlant(location);
        if (!hasEatenPlant) {
            tryEatAnimal(location, context);
        }

        if (!hasEatenPlant) {
            this.loseWeight(Config.BASE_HUNGER_LOSS, context);
        }
    }

    private boolean tryEatPlant(Location location) {
        Optional<Plant> plantOpt = location.findPlantForEating();

        if (plantOpt.isPresent()) {
            Plant plant = plantOpt.get();
            double desired = this.getType().getFoodRequired();
            double available = plant.getWeight();
            double eaten = Math.min(desired, available);

            this.gainWeight(eaten);
            plant.decrementWeight(eaten);

            if (plant.getWeight() <= 0) {
                location.removePlant(plant);
            }

            return true;
        }
        return false;
    }

    private void tryEatAnimal(Location location, StepContext context) {
        Set<AnimalType> preyTypes = new HashSet<>();
        if(probabilityOfEating.containsKey(AnimalType.MOUSE)){
            preyTypes.add(AnimalType.MOUSE);
        }

        if(probabilityOfEating.containsKey(AnimalType.CATERPILLAR)){
            preyTypes.add(AnimalType.CATERPILLAR);
        }

        List<Animal> potentialPrey = location.findAnimalsByTypes(preyTypes);
        if (potentialPrey.isEmpty()) return;

        for (Animal prey : potentialPrey) {
            Integer probability = probabilityOfEating.get(prey.getType());
            if (probability == null) continue;

            if (ThreadLocalRandom.current().nextInt(100) < probability) {
                this.gainWeight(prey.getCurrentWeight());
                context.markAnimalForRemoval(prey);
            }
        }
    }
}
