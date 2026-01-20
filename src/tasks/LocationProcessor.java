package tasks;

import entities.Animal;
import island.Location;
import simulation.StepContext;

import java.util.concurrent.CountDownLatch;

public class LocationProcessor implements Runnable {
    private final Location location;
    private final StepContext context;
    private final CountDownLatch completionLatch;

    public LocationProcessor(Location location, StepContext context, CountDownLatch completionLatch) {
        this.location = location;
        this.context = context;
        this.completionLatch = completionLatch;
    }

    @Override
    public void run() {
        try {
            processEating();
            processReproduction();
            processPlantGrowth();
            processMovement();
            processAging();

        } catch (Exception e) {
            System.err.println("Ошибка в локации " + location + ": " + e.getMessage());
        } finally {
            completionLatch.countDown();
        }
    }

    private void processEating() {
        for (Animal animal : location.getAllAnimals()) {
            if (animal.isAlive()) {
                animal.eat(context);
            }
        }
    }

    private void processReproduction() {
        for (Animal animal : location.getAllAnimals()) {
            if (animal.isAlive()) {
                animal.reproduce(context);
            }
        }
    }

    private void processPlantGrowth() {
        location.growthOfPlants();
    }

    private void processMovement() {
        for (Animal animal : location.getAllAnimals()) {
            if (animal.isAlive()) {
                animal.move(context);
            }
        }
    }

    private void processAging() {
        for (Animal animal : location.getAllAnimals()) {
            if (animal.isAlive()) {
                animal.age(context);
            }
        }
    }
}