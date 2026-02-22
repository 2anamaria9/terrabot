package main.entities.Plant;

import fileio.PlantInput;

public final class GymnospermsPlants extends Plant {
    private static final double BASE_OXYGEN = 0.0;
    private static final double PROBABILITY_VALUE = 60.0;
    private static final double MAX_PERCENTAGE = 100.0;

    public GymnospermsPlants(final PlantInput plantInput) {
        super(plantInput);
    }

    /**
     * Gets the base oxygen production for Gymnosperms.
     *
     * @return The base oxygen amount (0.0 for Gymnosperms).
     */
    @Override
    public double getOxygen() {
        return BASE_OXYGEN;
    }

    /**
     * Calculates the probability of the robot getting stuck in Gymnosperms.
     *
     * @return The calculated probability (0.6).
     */
    @Override
    public double calculateProbability() {
        return PROBABILITY_VALUE / MAX_PERCENTAGE;
    }
}
