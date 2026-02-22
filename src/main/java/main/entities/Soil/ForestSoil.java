package main.entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Getter;

@Getter
public final class ForestSoil extends Soil {
    private static final double NITROGEN_WEIGHT = 1.2;
    private static final double ORGANIC_MATTER_WEIGHT = 2.0;
    private static final double RETENTION_QUALITY_WEIGHT = 1.5;
    private static final double LEAF_LITTER_QUALITY_WEIGHT = 0.3;
    private static final double RETENTION_PROB_WEIGHT = 0.6;
    private static final double LEAF_LITTER_PROB_WEIGHT = 0.4;
    private static final double PROBABILITY_DIVISOR = 80.0;
    private static final double MAX_PERCENTAGE = 100.0;

    private final double leafLitter;

    public ForestSoil(final SoilInput soilInput) {
        super(soilInput);
        this.leafLitter = soilInput.getLeafLitter();
    }

    /**
     * For ForestSoil, it adds the "leafLitter".
     *
     * @param node The ObjectNode to update.
     */
    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("leafLitter", leafLitter);
    }

    /**
     * Calculates the soil quality score for Forest Soil.
     *
     * @return The normalized and rounded quality score.
     */
    @Override
    public double calculateQuality() {
        double score = (nitrogen * NITROGEN_WEIGHT)
                + (organicMatter * ORGANIC_MATTER_WEIGHT)
                + (waterRetention * RETENTION_QUALITY_WEIGHT)
                + (leafLitter * LEAF_LITTER_QUALITY_WEIGHT);
        return round(normalize(score));
    }

    /**
     * Calculates the probability of the robot getting stuck in Forest Soil.
     *
     * @return The calculated rounded probability.
     */
    @Override
    public double calculateProbability() {
        double probability = ((waterRetention * RETENTION_PROB_WEIGHT)
                + (leafLitter * LEAF_LITTER_PROB_WEIGHT))
                / PROBABILITY_DIVISOR * MAX_PERCENTAGE;
        return round(probability);
    }
}
