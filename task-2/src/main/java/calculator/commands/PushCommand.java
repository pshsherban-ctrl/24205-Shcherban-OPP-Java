package calculator.commands;

import calculator.*; 
import java.util.*;

@CommandName("PUSH")
public class PushCommand implements Command {
    @Override
    public void execute(ExecutionContext context, String[] args) throws InvalidArgumentException {
        if (args.length < 1) throw new InvalidArgumentException("PUSH needs 1 arg");
        String arg = args[0].toUpperCase(); 
        
        try {
            if (context.getParameters().containsKey(arg)) {
                context.getStack().push(context.getParameters().get(arg));
            } else {
                double val = Double.parseDouble(args[0]);
                context.getStack().push(val);
            }
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Invalid number or parameter: " + args[0]);
        }
    }
}