package thread;

import model.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BuildTask implements Runnable {
    private final Storage<Body> bodyStorage;
    private final Storage<Motor> motorStorage;
    private final Storage<Accessory> accessoryStorage;
    private final Storage<Auto> autoStorage;
    private static final AtomicInteger autoIdGenerator = new AtomicInteger(0);
    private static final AtomicInteger totalBuiltCount = new AtomicInteger(0);

    public BuildTask(Storage<Body> b, Storage<Motor> m, Storage<Accessory> acc, Storage<Auto> auto) {
        this.bodyStorage = b;
        this.motorStorage = m;
        this.accessoryStorage = acc;
        this.autoStorage = auto;
    }

    @Override
    public void run() {
        try {
            Body body = bodyStorage.get();
            Motor motor = motorStorage.get();
            Accessory accessory = accessoryStorage.get();
            
            Auto auto = new Auto(autoIdGenerator.incrementAndGet(), body, motor, accessory);
            autoStorage.put(auto);
            totalBuiltCount.incrementAndGet();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static int getTotalBuiltCount() {
        return totalBuiltCount.get();
    }
} 
