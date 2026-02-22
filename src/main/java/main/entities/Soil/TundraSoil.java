package main.entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Getter;

@Getter
public final class TundraSoil extends Soil {
    private static final double NITROGEN_WEIGHT = 0.7;
    private static final double ORGANIC_MATTER_WEIGHT = 0.5;
    private static final double PERMAFROST_QUALITY_PENALTY = 1.5;
    private static final double BASE_CONST = 50.0;
    private static final double MAX_PERCENTAGE = 100.0;

    private final double permafrostDepth;

    public TundraSoil(final SoilInput soilInput) {
        super(soilInput);
        this.permafrostDepth = soilInput.getPermafrostDepth();
    }

    /**
     * For TundraSoil, it adds the "permafrostDepth".
     *
     * @param node The ObjectNode to update.
     */
    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("permafrostDepth", permafrostDepth);
    }

    /**
     * Calculates the soil quality score for Tundra Soil.
     *
     * @return The calculated and rounded quality score.
     */
    @Override
    public double calculateQuality() {
        double score = (nitrogen * NITROGEN_WEIGHT)
                + (organicMatter * ORGANIC_MATTER_WEIGHT)
                - (permafrostDepth * PERMAFROST_QUALITY_PENALTY);
        return round(normalize(score));
    }

    /**
     * Calculates the probability of the robot getting stuck in Tundra Soil.
     *
     * @return The calculated rounded probability.
     */
    @Override
    public double calculateProbability() {
        double probability = (BASE_CONST - permafrostDepth) / BASE_CONST * MAX_PERCENTAGE;
        return round(probability);
    }
}
