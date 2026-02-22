package main.entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Getter;

@Getter
public final class SwampSoil extends Soil {
    private static final double NITROGEN_WEIGHT = 1.1;
    private static final double ORGANIC_MATTER_WEIGHT = 2.2;
    private static final double WATER_LOGGING_PENALTY = 5.0;
    private static final double PROBABILITY_MULTIPLIER = 10.0;

    private final double waterLogging;

    public SwampSoil(final SoilInput soilInput) {
        super(soilInput);
        this.waterLogging = soilInput.getWaterLogging();
    }

    /**
     * For SwampSoil, it adds the "waterLogging".
     *
     * @param node The ObjectNode to update.
     */
    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("waterLogging", waterLogging);
    }

    /**
     * Calculates the soil quality score for Swamp Soil.
     *
     * @return The normalized and rounded quality score.
     */
    @Override
    public double calculateQuality() {
        double score = (nitrogen * NITROGEN_WEIGHT)
                + (organicMatter * ORGANIC_MATTER_WEIGHT)
                - (waterLogging * WATER_LOGGING_PENALTY);
        return round(normalize(score));
    }

    /**
     * Calculates the probability of the robot getting stuck in Swamp Soil.
     *
     * @return The calculated rounded probability.
     */
    @Override
    public double calculateProbability() {
        double probability = (waterLogging * PROBABILITY_MULTIPLIER);
        return round(probability);
    }
}
