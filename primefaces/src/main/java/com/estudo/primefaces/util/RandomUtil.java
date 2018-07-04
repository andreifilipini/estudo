package com.estudo.primefaces.util;

import java.util.Random;

public class RandomUtil {
    private static final Random RANDOM_FACTORY = new Random(System.currentTimeMillis());

    private RandomUtil() {
    }

    public static long nextLong() {
        return RANDOM_FACTORY.nextLong();
    }
}
