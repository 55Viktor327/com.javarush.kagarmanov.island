package entities;

import config.Config;
import entities.enums.AnimalType;
import entities.enums.Direction;
import entities.enums.Gender;
import factory.AnimalFactory;
import island.Island;
import island.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Predator extends Animal{
    public final Map<AnimalType, Map<AnimalType, Integer>> probabilityOfEating = Config.PROBABILITY_OF_EATING;

    public Predator(AnimalType type, Gender gender, Location location) {
        super(type, gender, location);
    }

    @Override
    protected void eat(Eatable food) {
        if(food instanceof Plant){
            return;
        }

        Animal prey = (Animal)food;
        double probability = probabilityOfEating
                .get(this.getType())
                .get(prey.getType());
        if((Math.random()*100.0) <= probability){
            double nutrition = prey.getCurrentWeight();
            this.gainWeight(nutrition);
            prey.die();

            Location location = this.getLocation();
            if (location != null) {
                location.tryRemoveAnimal(prey);
            }

        }else {
            this.loseWeight((getCurrentWeight()*0.1));
            System.out.println("Жертва убежала");
        }
    }

    @Override
    protected void move(){
        int maxSteps = this.getType().getMaxSpeed();
        for (int step = 0; step < maxSteps; step++) {
            boolean moved = makeOneStep();
            if(!moved){
                break;
            }
        }
    }

    private boolean makeOneStep() {
        // 1. Получаем текущую локацию
        Location currentLocation = this.getLocation();
        if (currentLocation == null) return false;

        // 2. Определяем возможные направления
        List<Direction> possibleDirections = getPossibleDirections(currentLocation);
        if (possibleDirections.isEmpty()) return false;

        // 3. Выбираем направление (случайное или по логике)
        Direction direction = chooseDirection(possibleDirections);

        // 4. Вычисляем новую позицию
        int newX = currentLocation.getCoordinateX() + direction.getDx();
        int newY = currentLocation.getCoordinateY() + direction.getDy();

        // 5. Получаем остров и целевую локацию
        Island island = Island.getIsland();
        Location targetLocation = island.getLocation(newX, newY);

        // 6. Пытаемся переместиться
        if (targetLocation != null && tryMoveToLocation(targetLocation)) {
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

    private boolean tryMoveToLocation(Location targetLocation) {
        // 1. Пытаемся добавить животное в новую локацию
        boolean added = targetLocation.tryAddAnimal(this);

        if (added) {
            // 2. Если успешно - удаляем из старой локации
            Location currentLocation = this.getLocation();
            currentLocation.tryRemoveAnimal(this);

            // 3. Обновляем ссылку у животного
            this.setLocation(targetLocation);
            return true;
        }
        return false; // Не смогли добавить (нет места)
    }

    @Override
    protected Optional<Animal> reproduce(Animal partner) {
        if(!canReproduce(partner)) {
            return Optional.empty();
        }

        Optional<Animal> cub = (this.getGender() == Gender.FEMALE) ? createCub(this) : createCub(partner);
        cub.ifPresent(c -> {
            this.setReproductionCooldown(this.getType().getReproductionCooldown());
            partner.setReproductionCooldown(partner.getType().getReproductionCooldown());});
        return cub;
    }

    private Optional<Animal> createCub(Animal mother){
        if(Math.random() <= 0.6){
            return Optional.of(AnimalFactory.createAnimal(mother.getType(), mother.getLocation()));
        }
        return Optional.empty();
    }

    private boolean canReproduce(Animal partner){
        if(this.getType() == partner.getType()
            && this.getGender() != partner.getGender()
            && this.getHealth() >= 80
            && partner.getHealth() >= 80
            && this.getReproductionCooldown() == 0
            && partner.getReproductionCooldown() == 0){
            return true;
        }
        return false;
    }
}
