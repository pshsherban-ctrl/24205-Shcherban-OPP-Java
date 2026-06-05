package pool;

import java.util.LinkedList;
import java.util.Queue;

public class ThreadPool {
    private final Queue<Runnable> taskQueue = new LinkedList<>();
    private final WorkerThread[] workers;
    private boolean isShutdown = false;

    public ThreadPool(int numberOfThreads) {
        workers = new WorkerThread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            workers[i] = new WorkerThread();
            workers[i].start();
        }
    }

    public synchronized void execute(Runnable task) {
        if (isShutdown) return;
        taskQueue.add(task);
        notify();
    }

    public synchronized int getTaskCount() {
        return taskQueue.size();
    }

    private synchronized Runnable nextTask() throws InterruptedException {
        while (taskQueue.isEmpty() && !isShutdown) {
            wait();
        }
        return taskQueue.poll();
    }

    public void shutdown() {
        isShutdown = true;
        for (WorkerThread worker : workers) {
            worker.interrupt();
        }
    }

    private class WorkerThread extends Thread {
        @Override
        public void run() {
            try {
                while (!isInterrupted() && !isShutdown) {
                    Runnable task = nextTask();
                    if (task != null) {
                        task.run();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
} 
