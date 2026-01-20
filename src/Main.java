import tasks.TasksManager;

public class Main{
    public static void main(String[] args) {
        System.out.println("=== СИМУЛЯЦИЯ ЭКОСИСТЕМЫ ОСТРОВА ===");

        TasksManager manager = new TasksManager();

        manager.startSimulation();
    }
}
