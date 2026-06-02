package calculator;

public interface Command {
    void execute(ExecutionContext context, String[] args) throws CalculatorException;
} 
