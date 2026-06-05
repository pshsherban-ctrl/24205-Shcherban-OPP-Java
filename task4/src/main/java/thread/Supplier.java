package thread;

import model.Storage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class Supplier<T> extends Thread {
    private final Storage<T> storage;
    private final Function<Integer, T> factory;
    private final AtomicInteger idGenerator;
    private volatile int delay = 1000;

    public Supplier(Storage<T> storage, Function<Integer, T> factory, AtomicInteger idGenerator) {
        this.storage = storage;
        this.factory = factory;
        this.idGenerator = idGenerator;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Thread.sleep(delay);
                T item = factory.apply(idGenerator.incrementAndGet());
                storage.put(item);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 
