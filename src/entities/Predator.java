package entities;

import config.Config;
import entities.enums.AnimalType;
import entities.enums.Direction;
import entities.enums.Gender;
import factory.AnimalFactory;
import island.Island;
import island.Location;
import simulation.StepContext;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Predator extends Animal{
    public final Map<AnimalType, Map<AnimalType, Integer>> probabilityOfEating = Config.PROBABILITY_OF_EATING;

    public Predator(AnimalType type, Gender gender, Location location) {
        super(type, gender, location);
    }

    @Override
    public void eat(StepContext context) {
        if (context == null || location == null) return;

        Map<AnimalType, Integer> preyMap = probabilityOfEating.get(this.getType());
        if (preyMap == null || preyMap.isEmpty()) {
            this.loseWeight(Config.BASE_HUNGER_LOSS, context);
            return;
        }

        Set<AnimalType> preyTypes = preyMap.keySet();
        List<Animal> potentialPrey = location.findAnimalsByTypes(preyTypes);

        if (potentialPrey.isEmpty()) {
            this.loseWeight(Config.BASE_HUNGER_LOSS, context);
            return;
        }

        boolean hasEaten = tryEatRandomPrey(potentialPrey, preyMap, context);

        if (!hasEaten) {
            this.loseWeight(Config.BASE_HUNGER_LOSS, context);
        }
    }

    private boolean tryEatRandomPrey(List<Animal> potentialPrey, Map<AnimalType, Integer> preyMap, StepContext context) {

        Collections.shuffle(potentialPrey);

        for (Animal prey : potentialPrey) {
            Integer probability = preyMap.get(prey.getType());
            if (probability == null) continue;

            if (ThreadLocalRandom.current().nextInt(100) < probability) {
                double nutrition = prey.getCurrentWeight();
                this.gainWeight(nutrition);
                context.markAnimalForRemoval(prey);
                return true;
            }
        }

        return false;
    }

    @Override
    public void move(StepContext context){
        int maxSteps = this.getType().getMaxSpeed();
        for (int step = 0; step < maxSteps; step++) {
            boolean moved = makeOneStep(context);
            if(!moved){
                break;
            }
        }
    }

    private boolean makeOneStep(StepContext context) {
        if(context == null) return false;

        Location currentLocation = this.getLocation();
        if (currentLocation == null) return false;
        List<Direction> possibleDirections = getPossibleDirections(currentLocation);
        if (possibleDirections.isEmpty()) return false;
        Direction direction = chooseDirection(possibleDirections);
        int newX = currentLocation.getCoordinateX() + direction.getDx();
        int newY = currentLocation.getCoordinateY() + direction.getDy();

        Island island = Island.getIsland();
        Location targetLocation = island.getLocation(newX, newY);
        if (targetLocation != null) {
            context.addMovementIntent(this, targetLocation);
            return true;
        }

        return false;
    }

    private List<Direction> getPossibleDirections(Location currentLocation) {
        List<Direction> possible = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            int newX = currentLocation.getCoordinateX() + dir.getDx();
            int newY = currentLocation.getCoordinateY() + dir.getDy();
            if (isValidCoordinate(newX, newY)) {
                possible.add(dir);
            }
        }

        return possible;
    }

    private boolean isValidCoordinate(int x, int y) {
        Island island = Island.getIsland();
        return x >= 0 && x < island.getWidthIsland() &&
                y >= 0 && y < island.getLengthIsland();
    }

    private Direction chooseDirection(List<Direction> possibleDirections) {
            return possibleDirections.get(ThreadLocalRandom.current().nextInt(possibleDirections.size()));
    }

    @Override
    public void reproduce(StepContext context) {
        if (context == null) return;

        Location location = this.getLocation();
        Optional<Animal> partnerOpt = location.findPartnerFor(this);
        partnerOpt.ifPresent(partner -> {
            Animal mother = (this.getGender() == Gender.FEMALE) ? this : partner;
            Optional<Animal> cubOpt = createCub(mother);

            cubOpt.ifPresent(cub -> {
                context.addNewborn(cub);
                this.setReproductionCooldown(this.getType().getReproductionCooldown());
                partner.setReproductionCooldown(partner.getType().getReproductionCooldown());
            });
        });
    }

    private Optional<Animal> createCub(Animal mother){
        if(Math.random() <= 0.6){
            return Optional.of(AnimalFactory.createAnimal(mother.getType(), mother.getLocation()));
        }
        return Optional.empty();
    }
}
