package tasks;

import entities.enums.AnimalType;
import island.Island;

import java.util.Map;

public class StatisticsOfIsland implements Runnable{
    private Island island;

    public StatisticsOfIsland(Island island){
        this.island = island;
    }

    @Override
    public void run() {
        System.out.println("======== Статистика острова ========");
        System.out.println("Популяция животных составляет: ");

        Map<AnimalType, Integer> populations = island.getCurrentPopulationOfAnimals();
        for(Map.Entry<AnimalType, Integer> entry : populations.entrySet())

        System.out.printf("%s: %d %n", entry.getKey(), entry.getValue());
        System.out.println();
    }
}
