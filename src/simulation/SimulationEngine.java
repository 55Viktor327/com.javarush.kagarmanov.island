package simulation;

import island.Island;
import island.Location;
import tasks.LocationProcessor;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationEngine implements Runnable {
    private final Island island;
    private final ExecutorService locationExecutor;
    private volatile boolean running = false;
    private final AtomicInteger currentCycle = new AtomicInteger(0);

    public SimulationEngine() {
        this.island = Island.getIsland();
        int threadCount = Runtime.getRuntime().availableProcessors();
        this.locationExecutor = Executors.newFixedThreadPool(threadCount);

        System.out.println("Инициализация острова:");
        System.out.println("  Остров: " + island.getWidthIsland() + "x" + island.getLengthIsland());
        System.out.println("  Локаций: " + island.getAllLocations().size());
        System.out.println("  Потоков: " + threadCount);
    }

    @Override
    public void run() {
        running = true;
        System.out.println("\n=== СИМУЛЯЦИЯ НАЧАЛАСЬ ===");

        try {
            while (running && !Thread.currentThread().isInterrupted()) {
                executeOneCycle();
            }
        } catch (Exception e) {
            System.err.println("Критическая ошибка в симуляции: " + e.getMessage());
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    private void executeOneCycle() {
        int cycleNum = currentCycle.incrementAndGet();
        System.out.println("\n" + "=".repeat(50));
        System.out.println("ЦИКЛ " + cycleNum);
        System.out.println("=".repeat(50));

        StepContext context = new StepContext();

        List<Location> allLocations = island.getAllLocations();
        int totalLocations = allLocations.size();

        CountDownLatch completionLatch = new CountDownLatch(totalLocations);

        for (Location location : allLocations) {
            LocationProcessor processor = new LocationProcessor(location, context, completionLatch);
            locationExecutor.submit(processor);
        }

        try {
            completionLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        context.applyChanges();
        printStatistics();
    }

    private void printStatistics() {
        System.out.println("\n--- СТАТИСТИКА ОСТРОВА ---");
        System.out.println("Популяция животных составляет:");

        island.getCurrentPopulationOfAnimals().forEach((type, count) -> {
            if (count > 0) {
                System.out.printf("  %-15s: %d%n", type, count);
            }
        });

        int totalPlants = 0;
        for (Location location : island.getAllLocations()) {
            totalPlants += location.getPlants().size();
        }

        System.out.println("Растений на острове: " + totalPlants);
        System.out.println();

        int totalAnimals = island.getCurrentPopulationOfAnimals().values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        System.out.println("  Всего животных: " + totalAnimals);


    }

    public void stop() {
        running = false;
    }

    private void shutdown() {
        locationExecutor.shutdown();
        try {
            if (!locationExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
                locationExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            locationExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public int getCurrentCycle() {
        return currentCycle.get();
    }
}