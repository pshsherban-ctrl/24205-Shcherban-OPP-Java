package thread;

import model.Auto;
import model.Storage;
import pool.ThreadPool;

public class WarehouseController extends Thread {
    private final Storage<Auto> autoStorage;
    private final ThreadPool threadPool;
    private final Runnable buildTask;

    public WarehouseController(Storage<Auto> autoStorage, ThreadPool threadPool, Runnable buildTask) {
        this.autoStorage = autoStorage;
        this.threadPool = threadPool;
        this.buildTask = buildTask;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                int currentAutos;
                int currentTasks;
                int maxCapacity;
                
                synchronized (autoStorage) {
                    currentAutos = autoStorage.size();
                    currentTasks = threadPool.getTaskCount();
                    maxCapacity = autoStorage.getCapacity();
                    
                    int needed = maxCapacity - (currentAutos + currentTasks);
                    for (int i = 0; i < needed; i++) {
                        threadPool.execute(buildTask);
                    }
                    
                    autoStorage.wait();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 
