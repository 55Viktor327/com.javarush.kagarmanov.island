package tasks;

import factory.AnimalFactory;
import factory.PlantFactory;
import island.Island;
import simulation.SimulationEngine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TasksManager {
    private ExecutorService engineExecutor;
    private SimulationEngine simulationEngine;
    private Future<?> simulationFuture;

    public TasksManager() {
        this.engineExecutor = Executors.newSingleThreadExecutor();
    }

    public void startSimulation() {
        System.out.println("Запуск симуляции...");

        Island island = Island.getIsland();
        AnimalFactory animalFactory = new AnimalFactory();
        PlantFactory plantFactory = new PlantFactory();

        island.initialize(animalFactory, plantFactory);


        simulationEngine = new SimulationEngine();

        simulationFuture = engineExecutor.submit(simulationEngine);

        System.out.println("Симуляция успешно запущена");
    }
}
