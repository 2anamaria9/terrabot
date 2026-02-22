package main.entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Getter;

@Getter
public final class GrasslandSoil extends Soil {
    private static final double NITROGEN_WEIGHT = 1.3;
    private static final double ORGANIC_MATTER_WEIGHT = 1.5;
    private static final double ROOT_DENSITY_QUALITY_WEIGHT = 0.8;
    private static final double BASE_CONST = 50.0;
    private static final double RETENTION_WEIGHT = 0.5;
    private static final double PROBABILITY_DIVISOR = 75.0;
    private static final double MAX_PERCENTAGE = 100.0;

    private final double rootDensity;

    public GrasslandSoil(final SoilInput soilInput) {
        super(soilInput);
        this.rootDensity = soilInput.getRootDensity();
    }

    /**
     * For GrasslandSoil, it adds the "rootDensity".
     *
     * @param node The ObjectNode to update.
     */
    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("rootDensity", rootDensity);
    }

    /**
     * Calculates the soil quality score for Grassland Soil.
     *
     * @return The normalized and rounded quality score.
     */
    @Override
    public double calculateQuality() {
        double score = (nitrogen * NITROGEN_WEIGHT)
                + (organicMatter * ORGANIC_MATTER_WEIGHT)
                + (rootDensity * ROOT_DENSITY_QUALITY_WEIGHT);
        return round(normalize(score));
    }

    /**
     * Calculates the probability of the robot getting stuck in Grassland Soil.
     *
     * @return The calculated rounded probability.
     */
    @Override
    public double calculateProbability() {
        double probability = ((BASE_CONST - rootDensity) + (waterRetention * RETENTION_WEIGHT))
                / PROBABILITY_DIVISOR * MAX_PERCENTAGE;
        return round(probability);
    }
}
