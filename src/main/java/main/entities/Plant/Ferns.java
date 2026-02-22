package main.entities.Plant;

import fileio.PlantInput;

public final class Ferns extends Plant {
    private static final double BASE_OXYGEN = 0.0;
    private static final double PROBABILITY_VALUE = 30.0;
    private static final double MAX_PERCENTAGE = 100.0;

    public Ferns(final PlantInput plantInput) {
        super(plantInput);
    }

    /**
     * Gets the base oxygen production for Ferns.
     *
     * @return The base oxygen amount (0.0 for Ferns).
     */
    @Override
    public double getOxygen() {
        return BASE_OXYGEN;
    }

    /**
     * Calculates the probability of the robot getting stuck in Ferns.
     *
     * @return The calculated probability (0.3).
     */
    @Override
    public double calculateProbability() {
        return PROBABILITY_VALUE / MAX_PERCENTAGE;
    }
}
