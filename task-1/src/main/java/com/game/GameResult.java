package com.game;

// Класс для хранения результата хода.
public record GameResult(int bulls, int cows) {
    @Override
    public String toString() {
        return String.format("%d быков, %d коров", bulls, cows);
    }
} 
