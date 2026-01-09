package tasks;

import island.Island;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TasksManager {
    private final ScheduledExecutorService scheduler;
    private final Map<String, ScheduledFuture<?>> tasks;

    public TasksManager(){
        this.scheduler = Executors.newScheduledThreadPool(10);
        this.tasks = new HashMap<>();
    }

    public void startSimulation(){
        Island island = Island.getIsland();
        scheduleTask("Статистика острова", new StatisticsOfIsland(island), 0, 10, TimeUnit.SECONDS);
    }

    private void scheduleTask(String name, Runnable task, int delay, int period, TimeUnit seconds) {
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(task, delay, period, TimeUnit.SECONDS);
        tasks.put(name, future);
    }

    public void stopSimulation() {
        tasks.values().forEach(future -> future.cancel(true));
        scheduler.shutdown();
    }
}
