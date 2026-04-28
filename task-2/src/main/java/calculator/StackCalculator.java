package calculator;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class StackCalculator {
    private static final Logger logger = Logger.getLogger(StackCalculator.class.getName());
    private final CommandFactory factory = new CommandFactory();
    private final ExecutionContext context = new ExecutionContext();

    public void execute(InputStream in) {
        Scanner scanner = new Scanner(in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            String[] tokens = line.split("\\s+");
            String commandName = tokens[0];
            String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

            try {
                Command command = factory.getCommand(commandName);
                if (command == null) {
                    logger.warning("Unknown command: " + commandName);
                    continue;
                }
                command.execute(context, args);
            } catch (CalculatorException e) {
                logger.log(Level.WARNING, "Command error: " + e.getMessage());
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        StackCalculator calc = new StackCalculator();
        try {
            InputStream is = (args.length > 0) ? new FileInputStream(args[0]) : System.in;
            calc.execute(is);
        } catch (FileNotFoundException e) {
            logger.severe("Input file not found: " + e.getMessage());
        }
    }
} 
