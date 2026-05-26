package calculator.commands;

import calculator.*; 
import java.util.*;

@CommandName("PRINT")
public class PrintCommand implements Command {
    @Override
    public void execute(ExecutionContext context, String[] args) throws StackSizeException {
        if (context.getStack().isEmpty()) {
            throw new StackSizeException("Stack is empty, cannot PRINT");
        }
        System.out.println(context.getStack().peek());
    }
} 
