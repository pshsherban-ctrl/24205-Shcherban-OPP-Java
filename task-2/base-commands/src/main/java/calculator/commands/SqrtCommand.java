package calculator.commands;

import calculator.*; 
import java.util.*;

@CommandName("SQRT")
public class SqrtCommand implements Command {
    public void execute(ExecutionContext context, String[] args) throws StackSizeException {
        if (context.getStack().isEmpty()) throw new StackSizeException("Stack empty for SQRT");
        double val = context.getStack().pop();
        context.getStack().push(Math.sqrt(val));
    }
} 
