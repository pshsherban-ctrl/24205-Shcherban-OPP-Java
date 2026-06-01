package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private int storageBodySize;
    private int storageMotorSize;
    private int storageAccessorySize;
    private int storageAutoSize;
    private int accessorySuppliers;
    private int workers;
    private int dealers;
    private boolean logSale;

    public Config(String filePath) throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            props.load(fis);
        }
        storageBodySize = Integer.parseInt(props.getProperty("StorageBodySize", "100"));
        storageMotorSize = Integer.parseInt(props.getProperty("StorageMotorSize", "100"));
        storageAccessorySize = Integer.parseInt(props.getProperty("StorageAccessorySize", "100"));
        storageAutoSize = Integer.parseInt(props.getProperty("StorageAutoSize", "100"));
        accessorySuppliers = Integer.parseInt(props.getProperty("AccessorySuppliers", "1"));
        workers = Integer.parseInt(props.getProperty("Workers", "1"));
        dealers = Integer.parseInt(props.getProperty("Dealers", "1"));
        logSale = Boolean.parseBoolean(props.getProperty("LogSale", "false"));
    }

    public int getStorageBodySize() { return storageBodySize; }
    public int getStorageMotorSize() { return storageMotorSize; }
    public int getStorageAccessorySize() { return storageAccessorySize; }
    public int getStorageAutoSize() { return storageAutoSize; }
    public int getAccessorySuppliers() { return accessorySuppliers; }
    public int getWorkers() { return workers; }
    public int getDealers() { return dealers; }
    public boolean isLogSale() { return logSale; }
} 
