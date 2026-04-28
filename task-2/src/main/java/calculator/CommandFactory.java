package calculator;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class CommandFactory {
    private final Map<String, Command> commands = new HashMap<>();
    private static final Logger logger = Logger.getLogger(CommandFactory.class.getName());

    public CommandFactory() {
        loadCommandsFromConfig();
    }

    private void loadCommandsFromConfig() {
        try (InputStream is = getClass().getResourceAsStream("commands.cfg")) {
            if (is == null) {
                logger.severe("Config file commands.cfg not found in resources!");
                return;
            }
            
            Scanner scanner = new Scanner(is);
            while (scanner.hasNextLine()) {
                String className = scanner.nextLine().trim();
                
                if (className.isEmpty() || className.startsWith("#")) continue;
                
                try {
                    // Загружаем класс по его полному имени calculator.commands.PushCommand
                    Class<?> clazz = Class.forName(className);
                    registerCommand(clazz);
                } catch (ClassNotFoundException e) {
                    logger.severe("Could not find class: " + className);
                } catch (Exception e) {
                    logger.severe("Error registering class " + className + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.severe("Initialization error: " + e.getMessage());
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