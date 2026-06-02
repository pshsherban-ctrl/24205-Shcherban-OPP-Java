package calculator;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Logger;

public class CommandFactory {
    private final Map<String, Command> commands = new HashMap<>();
    private static final Logger logger = Logger.getLogger(CommandFactory.class.getName());

    public CommandFactory() {
        loadCommandsFromConfig();
    }

    private void loadCommandsFromConfig() {
        try (InputStream is = getClass().getResourceAsStream("commands.cfg")) {
            if (is == null) throw new IOException("Config file commands.cfg not found");
            
            Scanner scanner = new Scanner(is);
            while (scanner.hasNextLine()) {
                String jarPath = scanner.nextLine().trim();
                if (jarPath.isEmpty() || jarPath.startsWith("#")) continue;
                
                try {
                    loadJar(jarPath);
                } catch (Exception e) {
                    logger.severe("Error loading JAR " + jarPath + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.severe("Initialization error: " + e.getMessage());
        }
    }

    private void loadJar(String jarPath) throws Exception {
        File file = new File(jarPath);
        if (!file.exists()) {
            logger.severe("JAR file not found: " + jarPath);
            return;
        }

        URL url = file.toURI().toURL();
        //  загрузчик классов для JAR
        URLClassLoader loader = new URLClassLoader(new URL[]{url}, getClass().getClassLoader());
        logger.info("Scanning JAR for commands: " + jarPath);

        //  поток для чтения JAR-файла
        try (JarInputStream jis = new JarInputStream(new FileInputStream(file))) {
            JarEntry entry;
            while ((entry = jis.getNextJarEntry()) != null) {
                String name = entry.getName();
                
                if (name.endsWith(".class")) {
                    // Преобразуем путь файла в полное имя класса (calculator.commands.AddCommand)
                    String className = name.substring(0, name.length() - 6).replace('/', '.');
                    
                    try {
                        Class<?> clazz = loader.loadClass(className);
                        registerCommand(clazz);
                    } catch (ClassNotFoundException | NoClassDefFoundError e) {
                    }
                }
            }
        }
    }

    public void registerCommand(Class<?> clazz) throws Exception {
        if (Command.class.isAssignableFrom(clazz)) {
            if (clazz.isAnnotationPresent(CommandName.class)) {
                CommandName annotation = clazz.getAnnotation(CommandName.class);
                String commandName = annotation.value().toUpperCase();
            
                Command commandInstance = (Command) clazz.getDeclaredConstructor().newInstance();
                commands.put(commandName, commandInstance);
            
                logger.info("Successfully registered command: " + commandName);
            } else {
                logger.warning("Class " + clazz.getName() + " is missing @CommandName annotation");
            }
        }
    }   

    public Command getCommand(String name) {
        return commands.get(name.toUpperCase());
    }
}