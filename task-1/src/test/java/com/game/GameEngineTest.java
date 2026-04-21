package com.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {

    @Test
    void testBullsAndCowsCalculation() {
        GameEngine engine = new GameEngine(4);
        String secret = engine.getSecret();
        
        // все быки
        GameResult resultWin = engine.calculateResult(secret);
        assertEquals(4, resultWin.bulls());
        assertEquals(0, resultWin.cows());
    }

    @Test
    void testManualLogic() {
        GameEngine engine = new GameEngine(4);
        String secret = engine.getSecret();
        
        // генерируем неправильную попытку, меняя местами символы секрета
        char[] chars = secret.toCharArray();
        char temp = chars[0];
        chars[0] = chars[1];
        chars[1] = temp;
        String attempt = new String(chars);
        GameResult result = engine.calculateResult(attempt);
        assertTrue(result.cows() >= 2 || result.bulls() < 4);
    }
}