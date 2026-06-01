import config.Config;
import model.*;
import pool.ThreadPool;
import thread.*;
import view.FactoryFrame;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        try {
            

            Config config = new Config("config.properties");

            Storage<Body> bodyStorage = new Storage<>(config.getStorageBodySize());
            Storage<Motor> motorStorage = new Storage<>(config.getStorageMotorSize());
            Storage<Accessory> accessoryStorage = new Storage<>(config.getStorageAccessorySize());
            Storage<Auto> autoStorage = new Storage<>(config.getStorageAutoSize());

            ThreadPool pool = new ThreadPool(config.getWorkers());

            AtomicInteger bodyIdGen = new AtomicInteger(0);
            AtomicInteger motorIdGen = new AtomicInteger(0);
            AtomicInteger accIdGen = new AtomicInteger(0);

            List<Supplier<Body>> bodySuppliers = new ArrayList<>();
            Supplier<Body> bSup = new Supplier<>(bodyStorage, Body::new, bodyIdGen);
            bodySuppliers.add(bSup);
            bSup.start();

            List<Supplier<Motor>> motorSuppliers = new ArrayList<>();
            Supplier<Motor> mSup = new Supplier<>(motorStorage, Motor::new, motorIdGen);
            motorSuppliers.add(mSup);
            mSup.start();

            List<Supplier<Accessory>> accSuppliers = new ArrayList<>();
            for (int i = 0; i < config.getAccessorySuppliers(); i++) {
                Supplier<Accessory> aSup = new Supplier<>(accessoryStorage, Accessory::new, accIdGen);
                accSuppliers.add(aSup);
                aSup.start();
            }

            List<Dealer> dealers = new ArrayList<>();
            for (int i = 0; i < config.getDealers(); i++) {
                Dealer dealer = new Dealer(autoStorage, i + 1, config.isLogSale());
                dealers.add(dealer);
                dealer.start();
            }

            BuildTask buildTask = new BuildTask(bodyStorage, motorStorage, accessoryStorage, autoStorage);
            WarehouseController controller = new WarehouseController(autoStorage, pool, buildTask);
            controller.start();

            javax.swing.SwingUtilities.invokeLater(() -> new FactoryFrame(
                    bodyStorage, motorStorage, accessoryStorage, autoStorage, pool,
                    bodySuppliers, motorSuppliers, accSuppliers, dealers
            ));

        } catch (IOException e) {
            System.err.println("Failed to load config file: " + e.getMessage());
        }
    }
}