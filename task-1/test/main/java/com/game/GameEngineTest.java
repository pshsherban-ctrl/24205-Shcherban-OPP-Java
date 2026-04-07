package com.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {
    private final GameEngine engine = new GameEngine();

    @Test
    void testCalculation() {
        GameResult result = engine.calculateResult("1234", "1354");
        // 1 и 4 на своих местах (2 быка), 3 есть, но не там (1 корова)
        assertEquals(2, result.bulls());
        assertEquals(1, result.cows());
    }

    @Test
    void testWin() {
        GameResult result = engine.calculateResult("5678", "5678");
        assertEquals(4, result.bulls());
    }
} 
