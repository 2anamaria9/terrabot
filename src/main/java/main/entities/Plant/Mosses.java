package main.entities.Plant;

import fileio.PlantInput;

public final class Mosses extends Plant {
    private static final double BASE_OXYGEN = 0.8;
    private static final double PROBABILITY_VALUE = 40.0;
    private static final double MAX_PERCENTAGE = 100.0;

    public Mosses(final PlantInput plantInput) {
        super(plantInput);
    }

    /**
     * Gets the base oxygen production for Mosses.
     *
     * @return The base oxygen amount (0.8 for Mosses).
     */
    @Override
    public double getOxygen() {
        return BASE_OXYGEN;
    }

    /**
     * Calculates the probability of the robot getting stuck in Mosses.
     *
     * @return The calculated probability (0.4).
     */
    @Override
    public double calculateProbability() {
        return PROBABILITY_VALUE / MAX_PERCENTAGE;
    }
}
