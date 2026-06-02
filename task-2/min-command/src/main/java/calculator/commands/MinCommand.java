package calculator.commands;

import calculator.*;
import java.util.Deque;

@CommandName("MIN")
public class MinCommand implements Command {
    @Override
    public void execute(ExecutionContext context, String[] args) throws CalculatorException {
        Deque<Double> stack = context.getStack();

        if (stack.size() < 2) {
            throw new StackSizeException("Not enough elements on the stack for 'MIN' operation. Requires at least 2.");
        }
        
        double a = stack.pop();
        double b = stack.pop();
        
        double minResult = Math.min(a, b);
        stack.push(minResult);
    }
} 
