package com.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class GameEngine {
    private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);
    private final String secret;            
    private final int length;

    public GameEngine(int length) {
        this.length = length;
        this.secret = generateSecret(length);
        logger.debug("Сгенерирован секрет: {}", secret); 
    }
    //генерация секретного числа
    private String generateSecret(int len) {
        List<Integer> digits = new ArrayList<>();
        for (int i = 0; i <= 9; i++) digits.add(i);
        Collections.shuffle(digits);
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(digits.get(i));
        }
        return sb.toString();
    }
    //сравнивает попытку пользователя с секретным числом
    public GameResult calculateResult(String attempt) {
        int bulls = 0;
        int cows = 0;

        for (int i = 0; i < secret.length(); i++) {
            char s = secret.charAt(i);
            char a = attempt.charAt(i);
            if (s == a) {
                bulls++;
            } else if (secret.indexOf(a) != -1) {
                cows++;
            }
        }
        logger.info("Попытка игрока: {} | Результат: {}Б {}К", attempt, bulls, cows);
        return new GameResult(bulls, cows);
    }

    public String getSecret() {
        return secret;
    }
}