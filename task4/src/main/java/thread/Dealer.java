package thread;

import model.Auto;
import model.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dealer extends Thread {
    // 1. Создаем логгер для текущего класса (slf4j)
    private static final Logger logger = LoggerFactory.getLogger(Dealer.class);
    
    private final Storage<Auto> autoStorage;
    private final int id;
    private final boolean logSale;
    private volatile int delay = 1000;

    public Dealer(Storage<Auto> autoStorage, int id, boolean logSale) {
        this.autoStorage = autoStorage;
        this.id = id;
        this.logSale = logSale;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Thread.sleep(delay);
                Auto auto = autoStorage.get();
                if (logSale) {
                    logPurchase(auto);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void logPurchase(Auto auto) {
        logger.info("Dealer {}: Auto {} (Body: {}, Motor: {}, Accessory: {})",
                this.id, 
                auto.getId(),
                auto.getBody().getId(), 
                auto.getMotor().getId(), 
                auto.getAccessory().getId());
    }
}