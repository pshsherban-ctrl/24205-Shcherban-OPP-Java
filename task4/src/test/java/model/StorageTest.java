package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {

    @Test
    public void testPutAndGet() throws InterruptedException {
        Storage<String> storage = new Storage<>(5);
        storage.put("Element");
        
        assertEquals(1, storage.size());
        assertEquals("Element", storage.get());
        assertEquals(0, storage.size());
    }

    @Test
    public void testStorageBlockWhenFull() throws InterruptedException {
        Storage<Integer> storage = new Storage<>(1);
        storage.put(100);

        Thread blockingThread = new Thread(() -> {
            try {
                storage.put(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        blockingThread.start();
        Thread.sleep(200);

        assertEquals(Thread.State.WAITING, blockingThread.getState());
        assertEquals(1, storage.size());

        storage.get();
        Thread.sleep(200);

        assertEquals(Thread.State.TERMINATED, blockingThread.getState());
        assertEquals(1, storage.size());
    }

    @Test
    public void testStorageBlockWhenEmpty() throws InterruptedException {
        Storage<Integer> storage = new Storage<>(5);

        Thread blockingThread = new Thread(() -> {
            try {
                storage.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        blockingThread.start();
        Thread.sleep(200);

        assertEquals(Thread.State.WAITING, blockingThread.getState());

        storage.put(500);
        Thread.sleep(200);

        assertEquals(Thread.State.TERMINATED, blockingThread.getState());
    }
} 
