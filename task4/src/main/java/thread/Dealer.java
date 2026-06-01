package thread;

import model.Auto;
import model.Storage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Dealer extends Thread {
    private final Storage<Auto> autoStorage;
    private final int id;
    private final boolean logSale;
    private volatile int delay = 1000;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

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

    private static synchronized void logPurchase(int dealerId, Auto auto) {
        try (FileWriter fw = new FileWriter("sales.log", true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.printf("%s: Dealer %d: Auto %d (Body: %d, Motor: %d, Accessory: %d)%n",
                    LocalTime.now().format(dtf), dealerId, auto.getId(),
                    auto.getBody().getId(), auto.getMotor().getId(), auto.getAccessory().getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logPurchase(Auto auto) {
        logPurchase(this.id, auto);
    }
}