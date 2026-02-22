package main.entities.Plant;

import fileio.PlantInput;

public final class FloweringPlants extends Plant {
    private static final double BASE_OXYGEN = 6.0;
    private static final double PROBABILITY_VALUE = 90.0;
    private static final double MAX_PERCENTAGE = 100.0;

    public FloweringPlants(final PlantInput plantInput) {
        super(plantInput);
    }

    /**
     * Gets the base oxygen production for Flowering Plants.
     *
     * @return The base oxygen amount (6.0).
     */
    @Override
    public double getOxygen() {
        return BASE_OXYGEN;
    }

    /**
     * Calculates the probability of the robot getting stuck in Flowering Plants.
     *
     * @return The calculated probability (0.9).
     */
    @Override
    public double calculateProbability() {
        return PROBABILITY_VALUE / MAX_PERCENTAGE;
    }
}
