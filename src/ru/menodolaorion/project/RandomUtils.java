package ru.menodolaorion.project;

import java.util.Random;

public class RandomUtils {
    private static Random random = new Random();
    public static double getRandom(double startPosition, double endPosition) {
        return random.nextDouble() * (endPosition - startPosition) + startPosition;
    }

    public static int getRandom(int startPosition, int endPosition) {
        return (int) (random.nextDouble() * (endPosition - startPosition)) + startPosition;
    }
}
