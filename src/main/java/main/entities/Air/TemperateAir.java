package main.entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import fileio.CommandInput;
import lombok.Getter;

@Getter
public final class TemperateAir extends Air {
    private static final double OXYGEN_WEIGHT = 2.0;
    private static final double HUMIDITY_WEIGHT = 0.7;
    private static final double POLLEN_PENALTY_WEIGHT = 0.1;
    private static final double MAX_TEMPERATE_SCORE = 84.0;
    private static final double SPRING_PENALTY = -15.0;

    private final double pollenLevel;

    public TemperateAir(final AirInput airInput) {
        super(airInput);
        this.pollenLevel = airInput.getPollenLevel();
    }

    /**
     * For Temperate Air, it adds the "pollenLevel".
     *
     * @param node The ObjectNode to update.
     */
    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("pollenLevel", pollenLevel);
    }

    /**
     * Calculates the air quality score for Temperate Air.
     *
     * @return The normalized and rounded quality score.
     */
    @Override
    public double calculateQuality() {
        double score = (oxygenLevel * OXYGEN_WEIGHT)
                + (humidity * HUMIDITY_WEIGHT)
                - (pollenLevel * POLLEN_PENALTY_WEIGHT);
        return round(normalize(score + weatherInfluence));
    }

    /**
     * Returns the maximum score for Temperate Air.
     *
     * @return The max score (84 in this case).
     */
    @Override
    public double maxScore() {
        return MAX_TEMPERATE_SCORE;
    }

    /**
     * Applies a penalty if the season is Spring.
     *
     * @param command The command containing weather details.
     * @return The value to add to the quality score (negative in this case).
     */
    @Override
    public double calculateWeather(final CommandInput command) {
        if (command.getSeason() != null) {
            if (command.getSeason().equalsIgnoreCase("Spring")) {
                return SPRING_PENALTY;
            }
        }
        return 0;
    }
}
