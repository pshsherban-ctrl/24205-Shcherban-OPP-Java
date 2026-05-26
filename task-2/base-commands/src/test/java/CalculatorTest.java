package calculator;

import calculator.commands.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private ExecutionContext context;

    @BeforeEach
    void setUp() {
        context = new ExecutionContext();
    }

    @Test
    void testPushAndPop() throws CalculatorException {
        PushCommand push = new PushCommand();
        PopCommand pop = new PopCommand();
        push.execute(context, new String[]{"10.5"});
        assertEquals(1, context.getStack().size());
        assertEquals(10.5, context.getStack().peek());
        pop.execute(context, new String[0]);
        assertTrue(context.getStack().isEmpty());
    }

    @Test
    void testAddCommand() throws CalculatorException {
        AddCommand add = new AddCommand();
        context.getStack().push(10.0);
        context.getStack().push(20.0);
        add.execute(context, new String[0]);
        assertEquals(30.0, context.getStack().peek());
    }

    @Test
    void testSqrtCommand() throws CalculatorException {
        SqrtCommand sqrt = new SqrtCommand();
        context.getStack().push(16.0);
        sqrt.execute(context, new String[0]);
        assertEquals(4.0, context.getStack().peek());
    }

    @Test
    void testDefineCommand() throws CalculatorException {
        DefineCommand define = new DefineCommand();
        PushCommand push = new PushCommand();
        define.execute(context, new String[]{"a", "4"});
        push.execute(context, new String[]{"a"});
        assertEquals(4.0, context.getStack().peek());
        assertEquals(4.0, context.getParameters().get("A"));
    }

    @Test
    void testStackSizeException() {
        AddCommand add = new AddCommand();
        context.getStack().push(1.0);
        assertThrows(StackSizeException.class, () -> {
            add.execute(context, new String[0]);
        });
    }
} 
