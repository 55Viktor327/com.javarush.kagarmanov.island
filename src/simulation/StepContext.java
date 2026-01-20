package simulation;

import entities.Animal;
import island.Location;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StepContext {
    private final Queue<Runnable> highPriorityActions = new ConcurrentLinkedQueue<>();  // Удаления
    private final Queue<Runnable> mediumPriorityActions = new ConcurrentLinkedQueue<>(); // Перемещения
    private final Queue<Runnable> lowPriorityActions = new ConcurrentLinkedQueue<>();    // Добавления
    private final AtomicInteger pendingRemovals = new AtomicInteger(0);
    private final AtomicInteger pendingMovements = new AtomicInteger(0);
    private final AtomicInteger pendingBirths = new AtomicInteger(0);
    private final Object applyLock = new Object();

    public void markAnimalForRemoval(Animal animal) {
        if (animal == null || !animal.isAlive()) {
            return;
        }

        pendingRemovals.incrementAndGet();
        highPriorityActions.offer(() -> {
            try {
                Location location = animal.getLocation();
                if (location != null) {
                    location.tryRemoveAnimal(animal);
                }
                animal.die();

            } catch (Exception e) {
                System.err.println("Ошибка при удалении животного " + animal + ": " + e.getMessage());
            }
        });
    }

    public void addNewborn(Animal cub) {
        if (cub == null) {
            return;
        }

        pendingBirths.incrementAndGet();
        lowPriorityActions.offer(() -> {
            try {
                Location birthLocation = cub.getLocation();
                if (birthLocation != null) {
                    birthLocation.tryAddAnimal(cub);
                }
            } catch (Exception e) {
                System.err.println("Ошибка при добавлении новорожденного " + cub + ": " + e.getMessage());
            }
        });
    }

    public void addMovementIntent(Animal animal, Location target) {
        if (animal == null || target == null || !animal.isAlive()) {
            return;
        }

        pendingMovements.incrementAndGet();
        mediumPriorityActions.offer(() -> {
            try {
                Location current = animal.getLocation();
                if (current != null && animal.isAlive()) {
                    current.tryMoveAnimalTo(animal, target);
                }
            } catch (Exception e) {
                System.err.println("Ошибка при перемещении животного " + animal + ": " + e.getMessage());
            }
        });
    }

    public void applyChanges() {
        synchronized (applyLock) {
            int totalActions = pendingRemovals.get() + pendingMovements.get() + pendingBirths.get();
            if (totalActions == 0) {
                return;
            }

            System.out.printf("=== ПРИМЕНЕНИЕ ИЗМЕНЕНИЙ (%d действий) ===%n", totalActions);
            System.out.printf("  Удаления: %d, Перемещения: %d, Рождения: %d%n", pendingRemovals.get(), pendingMovements.get(), pendingBirths.get());
            long startTime = System.currentTimeMillis();
            processActionQueue(highPriorityActions, "удаления");
            processActionQueue(mediumPriorityActions, "перемещения");
            processActionQueue(lowPriorityActions, "рождения");
            long endTime = System.currentTimeMillis();
            System.out.printf("Изменения применены за %d мс%n", (endTime - startTime));
            resetCounters();
        }
    }

    private void processActionQueue(Queue<Runnable> queue, String actionName) {
        int processed = 0;
        int batchSize = 100;
        Runnable action;

        while ((action = queue.poll()) != null) {
            try {
                action.run();
                processed++;

            } catch (Exception e) {
                System.err.printf("Ошибка при выполнении действия %s #%d: %s%n",
                        actionName, processed, e.getMessage());
            }
        }

        if (processed > 0 && processed < batchSize) {
            System.out.printf("    Обработано %d действий (%s)%n", processed, actionName);
        }
    }

    private void resetCounters() {
        pendingRemovals.set(0);
        pendingMovements.set(0);
        pendingBirths.set(0);
    }

    public void clearAll() {
        synchronized (applyLock) {
            highPriorityActions.clear();
            mediumPriorityActions.clear();
            lowPriorityActions.clear();
            resetCounters();
            System.out.println("Все очереди StepContext очищены");
        }
    }
}
