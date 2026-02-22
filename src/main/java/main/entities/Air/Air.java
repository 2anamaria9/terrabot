package main.entities.Air;

import fileio.AirInput;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Air {
    private static final double MIN_MODERATE_QUALITY = 40.0;
    private static final double MIN_GOOD_QUALITY = 70.0;
    private static final double MAX_PERCENTAGE = 100.0;
    private static final double TOXICITY_THRESHOLD_RATIO = 0.8;
    private static final double ROUNDING_FACTOR = 100.0;

    private String type;
    private String name;
    private double mass;
    protected double humidity;
    protected double temperature;
    protected double oxygenLevel;
    protected double weatherInfluence = 0.0;
    protected int weatherDuration = 0;

    public Air(final AirInput airInput) {
        this.type = airInput.getType();
        this.name = airInput.getName();
        this.mass = airInput.getMass();
        this.humidity = airInput.getHumidity();
        this.temperature = airInput.getTemperature();
        this.oxygenLevel = airInput.getOxygenLevel();
    }
    /**
     * Adds specific fields to the JSON output node.
     * Implemented by subclasses to add their unique properties.
     *
     * @param node The ObjectNode to populate.
     */
    public abstract void addSpecificFields(ObjectNode node);

    /**
     * Calculates the quality score of the air.
     * Implemented by subclasses.
     *
     * @return The calculated quality score.
     */
    public abstract double calculateQuality();

    /**
     * Returns the maximum score for this air type.
     *
     * @return The max score value.
     */
    public abstract double maxScore();

    /**
     * Calculates the weather influence based on a command.
     *
     * @param command The command containing weather details.
     * @return The calculated weather bonus or penalty.
     */
    public abstract double calculateWeather(CommandInput command);

    /**
     * Sets a temporary weather influence on the air.
     *
     * @param influence The value to add to the quality score.
     * @param duration  The duration in simulation steps.
     */
    public void setWeather(final double influence, final int duration) {
        this.weatherInfluence = influence;
        this.weatherDuration = duration;
    }

    /**
     * Updates the duration of the current weather effect.
     * Decrements the timer and resets influence if time runs out.
     */
    public void updateWeatherDuration() {
        if (weatherDuration > 0) {
            weatherDuration--;
            if (weatherDuration == 0) {
                weatherInfluence = 0.0;
            }
        }
    }

    /**
     * Interprets the numeric quality score into a one-word description.
     *
     * @return "poor", "moderate", or "good".
     */
    public String interpretationQuality() {
        double airQualityScore = calculateQuality();
        if (airQualityScore < MIN_MODERATE_QUALITY) {
            return "poor";
        } else if (airQualityScore > MIN_GOOD_QUALITY) {
            return "good";
        } else {
            return "moderate";
        }
    }

    /**
     * Calculates the toxicity level of the air.
     *
     * @return The calculated toxicity score.
     */
    public double calculateToxicity() {
        double airQualityScore = this.calculateQuality();
        double maxScore = this.maxScore();
        if (maxScore == 0) {
            return 0;
        }
        double toxicityAQ = MAX_PERCENTAGE * (1.0 - (airQualityScore / maxScore));
        return round(normalize(toxicityAQ));
    }

    /**
     * Checks if the air is toxic to animals.
     *
     * @return true if toxicity exceeds the threshold ratio of max score.
     */
    public boolean checkToxicity() {
        return calculateToxicity() > (TOXICITY_THRESHOLD_RATIO * maxScore());
    }

    /**
     * Updates the humidity level.
     * Used by interactions with water.
     *
     * @param value The amount to add to humidity.
     */
    public void updateHumidity(final double value) {
        humidity += value;
        humidity = round(humidity);
    }

    /**
     * Updates the oxygen level.
     * Used by interactions with plants.
     *
     * @param value The amount to add to oxygen level.
     */
    public void updateOxygen(final double value) {
        oxygenLevel += value;
        oxygenLevel = round(oxygenLevel);
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
