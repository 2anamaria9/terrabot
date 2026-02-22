package main.entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Getter;
import main.entities.Entity;

@Getter
public abstract class Soil implements Entity {
    private static final double MIN_MODERATE_QUALITY = 40.0;
    private static final double MIN_GOOD_QUALITY = 70.0;
    private static final double MAX_PERCENTAGE = 100.0;
    private static final double ROUNDING_FACTOR = 100.0;

    private final String type;
    private final String name;
    private final double mass;
    protected double nitrogen;
    protected double waterRetention;
    protected double soilpH;
    protected double organicMatter;

    public Soil(final SoilInput soilInput) {
        this.type = soilInput.getType();
        this.name = soilInput.getName();
        this.mass = soilInput.getMass();
        this.nitrogen = soilInput.getNitrogen();
        this.waterRetention = soilInput.getWaterRetention();
        this.soilpH = soilInput.getSoilpH();
        this.organicMatter = soilInput.getOrganicMatter();
    }

    /**
     * Calculates the quality score of the soil.
     * Implemented by subclasses.
     *
     * @return The calculated quality score.
     */
    public abstract double calculateQuality();

    /**
     * Interprets the numeric quality score into a one-word description.
     *
     * @return "poor", "moderate", or "good".
     */
    public String interpretationQuality() {
        double soilQualityScore = calculateQuality();
        if (soilQualityScore < MIN_MODERATE_QUALITY) {
            return "poor";
        } else if (soilQualityScore > MIN_GOOD_QUALITY) {
            return "good";
        } else {
            return "moderate";
        }
    }

    /**
     * Adds specific fields to the JSON output node.
     * Implemented by subclasses to add their unique properties.
     *
     * @param node The ObjectNode to populate.
     */
    public abstract void addSpecificFields(ObjectNode node);

    /**
     * Calculates the probability of the robot getting stuck in this soil.
     * Implemented by subclasses.
     *
     * @return The calculated probability.
     */
    public abstract double calculateProbability();

    /**
     * Updates the water retention level of the soil.
     * Used when water interacts with the soil.
     *
     * @param value The amount to add to water retention.
     */
    public void updateWaterRetention(final double value) {
        waterRetention += value;
        waterRetention = round(waterRetention);
    }

    /**
     * Updates the organic matter content of the soil.
     * Used when animals fertilize the soil.
     *
     * @param value The amount to add to organic matter.
     */
    public void updateOrganicMatter(final double value) {
        organicMatter += value;
        organicMatter = round(organicMatter);
    }

    /**
     * Rounds a double value.
     *
     * @param value The value to round.
     * @return The rounded value.
     */
    protected double round(final double value) {
        return Math.round(value * ROUNDING_FACTOR) / ROUNDING_FACTOR;
    }

    /**
     * Normalizes a score to be in [0, 100].
     *
     * @param score The score to normalize.
     * @return The normalized score.
     */
    protected double normalize(final double score) {
        return Math.max(0, Math.min(MAX_PERCENTAGE, score));
    }
}
