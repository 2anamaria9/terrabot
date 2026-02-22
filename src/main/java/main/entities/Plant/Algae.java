package main.entities.Plant;

import fileio.PlantInput;

public final class Algae extends Plant {
    private static final double BASE_OXYGEN = 0.5;
    private static final double PROBABILITY_VALUE = 20.0;
    private static final double MAX_PERCENTAGE = 100.0;

    public Algae(final PlantInput plantInput) {
        super(plantInput);
    }

    /**
     * Gets the base oxygen production for Algae.
     *
     * @return The base oxygen amount (0.5 for Algae).
     */
    @Override
    public double getOxygen() {
        return BASE_OXYGEN;
    }

    /**
     * Calculates the probability of the robot getting stuck in Algae.
     *
     * @return The calculated probability (0.2).
     */
    @Override
    public double calculateProbability() {
        return PROBABILITY_VALUE / MAX_PERCENTAGE;
    }
}
