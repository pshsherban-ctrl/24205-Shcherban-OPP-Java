package calculator.commands;

import calculator.*; 
import java.util.*;

@CommandName("/")
public class DivCommand implements Command {
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        if (context.getStack().size() < 2) throw new StackSizeException("Not enough elements for '/'");
        double b = context.getStack().pop();
        if (b == 0) throw new CalculatorException("Division by zero");
        double a = context.getStack().pop();
        context.getStack().push(a / b);
    }
} 
