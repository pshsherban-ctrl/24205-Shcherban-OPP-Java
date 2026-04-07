package com.game;

import java.util.Scanner;

public class Main {
    private static final int TIME_LIMIT_SECONDS = 20;
    private static final int MAX_ATTEMPTS = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameEngine engine = new GameEngine();

        System.out.println("--- Игра Быки и Коровы ---");
        System.out.print("Выберите длину числа (3-6): ");
        int length = scanner.nextInt();
        scanner.nextLine(); // очистка буфера

        String secret = engine.generateSecret(length);
        int attemptsLeft = MAX_ATTEMPTS;
        boolean won = false;

        System.out.println("Число загадано! У вас " + MAX_ATTEMPTS + " попыток.");
        System.out.println("На каждый ход дается " + TIME_LIMIT_SECONDS + " секунд.");

        while (attemptsLeft > 0) {
            System.out.printf("\nПопыток осталось: %d. Ваш ход: ", attemptsLeft);
            
            long startTime = System.currentTimeMillis();
            String attempt = scanner.nextLine();
            long endTime = System.currentTimeMillis();

            if ((endTime - startTime) / 1000 > TIME_LIMIT_SECONDS) {
                System.out.println("Время вышло! Попытка потеряна.");
                attemptsLeft--;
                continue;
            }

            if (attempt.length() != length || !attempt.matches("\\d+")) {
                System.out.println("Ошибка: введите " + length + " цифр.");
                continue;
            }

            GameResult result = engine.calculateResult(secret, attempt);
            System.out.println("Результат: " + result);

            if (result.bulls() == length) {
                won = true;
                break;
            }
            attemptsLeft--;
        }

        if (won) {
            System.out.println("Вы победили.");
        } else {
            System.out.println("Игра окончена. Было загадано: " + secret);
        }
    }
} 
