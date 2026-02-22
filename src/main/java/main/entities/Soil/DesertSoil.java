package main.entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Getter;

@Getter
public final class DesertSoil extends Soil {
    private static final double NITROGEN_WEIGHT = 0.5;
    private static final double RETENTION_QUALITY_WEIGHT = 0.3;
    private static final double SALINITY_PENALTY_WEIGHT = 2.0;
    private static final double BASE_PROBABILITY = 100.0;

    private final double salinity;

    public DesertSoil(final SoilInput soilInput) {
        super(soilInput);
        this.salinity = soilInput.getSalinity();
    }

    /**
     * For DesertSoil, it adds the "salinity" field.
     *
     * @param node The ObjectNode to update.
     */
    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("salinity", salinity);
    }

    /**
     * Calculates the soil quality score for Desert Soil.
     *
     * @return The normalized and rounded quality score.
     */
    @Override
    public double calculateQuality() {
        double score = (nitrogen * NITROGEN_WEIGHT)
                + (waterRetention * RETENTION_QUALITY_WEIGHT)
                - (salinity * SALINITY_PENALTY_WEIGHT);
        return round(normalize(score));
    }

    /**
     * Calculates the probability of the robot getting stuck in Desert Soil.
     *
     * @return The calculated rounded probability.
     */
    @Override
    public double calculateProbability() {
        double probability = (BASE_PROBABILITY - waterRetention + salinity);
        return round(probability);
    }
}
