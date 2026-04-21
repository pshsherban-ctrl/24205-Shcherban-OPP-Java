package com.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final int TIME_LIMIT_SECONDS = 20;
    private static final int MAX_ATTEMPTS = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("--- Игра Быки и Коровы ---");
        System.out.print("Выберите длину последовательности (3-6): ");
        int length = scanner.nextInt();
        scanner.nextLine();

        GameEngine engine = new GameEngine(length);
        int attempts = 0;
        boolean isGuessed = false;

        logger.info("Начало новой сессии. Длина: {}, Лимит попыток: {}", length, MAX_ATTEMPTS);

        while (attempts < MAX_ATTEMPTS) {
            attempts++;
            System.out.printf("[%d/%d] Введите ваш вариант: ", attempts, MAX_ATTEMPTS);
            
            long start = System.currentTimeMillis();
            String input = scanner.nextLine();
            long duration = (System.currentTimeMillis() - start) / 1000;

            if (duration > TIME_LIMIT_SECONDS) {
                logger.warn("Попытка отклонена: превышен лимит времени ({} сек)", duration);
                continue;
            }

            if (input.length() != length || !input.matches("\\d+")) {
                System.out.println("Ошибка! Введите ровно " + length + " цифр.");
                attempts--; // Не засчитываем некорректный ввод
                continue;
            }

            GameResult result = engine.calculateResult(input);
            System.out.println("-> " + result);

            if (result.bulls() == length) {
                isGuessed = true;
                break;
            }
        }

        if (isGuessed) {
            logger.info("ПОБЕДА! Число угадано за {} попыток.", attempts);
        } else {
            logger.info("ПРОИГРЫШ. Попытки исчерпаны. Было задумано: {}", engine.getSecret());
        }
    }
}