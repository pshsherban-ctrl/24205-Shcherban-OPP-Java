package calculator;

import java.util.*;

public class ExecutionContext {
    private final Deque<Double> stack = new ArrayDeque<>();
    private final Map<String, Double> parameters = new HashMap<>();

    public Deque<Double> getStack() { return stack; }
    public Map<String, Double> getParameters() { return parameters; }
} 
