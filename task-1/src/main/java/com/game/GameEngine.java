 
package com.game;

import java.util.*;
import java.util.logging.*;

//Класс, отвечающий за логику игры
public class GameEngine {
    private static final Logger LOGGER = Logger.getLogger(GameEngine.class.getName());

    static {
        try {
            FileHandler fh = new FileHandler("game.log", true);
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
        } catch (Exception e) {
            System.err.println("Не удалось создать файл лога.");
        }
    }

    // Генерирует секретное число с неповторяющимися цифрами. @param length длина числа, @return строка с числом
    public String generateSecret(int length) {
        List<Integer> digits = new ArrayList<>();
        for (int i = 0; i <= 9; i++) digits.add(i);
        Collections.shuffle(digits);
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(digits.get(i));
        }
        String secret = sb.toString();
        LOGGER.info("Сгенерировано секретное число: " + secret);
        return secret;
    }
    
    //Сравнивает попытку пользователя с секретным числом
    public GameResult calculateResult(String secret, String attempt) {
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
        LOGGER.info(String.format("Попытка: %s -> Результат: %d быков, %d коров", attempt, bulls, cows));
        return new GameResult(bulls, cows);
    }
}