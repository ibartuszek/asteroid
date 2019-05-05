package hu.elte.asteroid.components;

import java.util.List;
import java.util.Random;

public enum AsteroidSize {
    BIG(10.0f, 100.0f),
    NORMAL(7.0f, 49.0f),
    SMALL(4.0f, 16.0f);

    public static final int RANDOM_PERCENTAGE_FACTOR = 20;
    public static final float PERCENTAGE_FACTOR = 100.0f;
    private static final List<AsteroidSize> SIZE_LIST = List.of(values());
    private static final Random RANDOM = new Random();
    private final float radius;
    private final float gravityFactor;

    AsteroidSize(final float radius, final float gravityFactor) {
        this.radius = radius;
        this.gravityFactor = gravityFactor;
    }

    public static AsteroidSize getRandomSize() {
        return SIZE_LIST.get(RANDOM.nextInt(SIZE_LIST.size()));
    }

    public static AsteroidSize getSmallerSize(final AsteroidSize asteroidSize) {
        AsteroidSize result = null;
        if (asteroidSize.equals(AsteroidSize.BIG)) {
            result = AsteroidSize.NORMAL;
        } else if (asteroidSize.equals(AsteroidSize.NORMAL)) {
            result = AsteroidSize.SMALL;
        }
        return result;
    }

    public float getRadius() {
        return radius;
    }

    public float getRandomRadius() {
        float randomPercentage = (
            RANDOM.nextInt(2 * RANDOM_PERCENTAGE_FACTOR) - RANDOM_PERCENTAGE_FACTOR)
            / PERCENTAGE_FACTOR;
        return radius * (1.00f + randomPercentage);
    }

    public float getGravityFactor() {
        return gravityFactor;
    }
}
