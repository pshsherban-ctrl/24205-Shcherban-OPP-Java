package calculator.commands;

import calculator.*; 
import java.util.*;

@CommandName("DEFINE")
public class DefineCommand implements Command {
    @Override
    public void execute(ExecutionContext context, String[] args) throws InvalidArgumentException {
        if (args.length < 2) throw new InvalidArgumentException("DEFINE needs 2 args");
        String name = args[0].toUpperCase();
        try {
            double value = Double.parseDouble(args[1]);
            context.getParameters().put(name, value);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Invalid value: " + args[1]);
        }
    }
}