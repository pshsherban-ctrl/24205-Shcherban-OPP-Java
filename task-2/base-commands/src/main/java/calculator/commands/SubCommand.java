package calculator.commands;

import calculator.*; 
import java.util.*;

@CommandName("-")
public class SubCommand implements Command {
    @Override
    public void execute(ExecutionContext context, String[] args) throws StackSizeException {
        if (context.getStack().size() < 2) throw new StackSizeException("Not enough elements for '-'");
        double b = context.getStack().pop();
        double a = context.getStack().pop();
        context.getStack().push(a - b);
    }
} 
