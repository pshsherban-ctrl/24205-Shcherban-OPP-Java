package calculator.commands;

import calculator.*; 
import java.util.*;

@CommandName("POP")
public class PopCommand implements Command {
    @Override
    public void execute(ExecutionContext context, String[] args) throws StackSizeException {
        if (context.getStack().isEmpty()) {
            throw new StackSizeException("Stack is empty, cannot POP");
        }
        context.getStack().pop();
    }
} 
