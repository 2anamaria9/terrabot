package main.entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import fileio.CommandInput;
import lombok.Getter;

@Getter
public final class MountainAir extends Air {
    private static final double ALTITUDE_DIVISOR = 1000.0;
    private static final double ALTITUDE_PENALTY_WEIGHT = 0.5;
    private static final double OXYGEN_FACTOR_WEIGHT = 2.0;
    private static final double HUMIDITY_WEIGHT = 0.6;
    private static final double MAX_MOUNTAIN_SCORE = 78.0;
    private static final double NEGATIVE_MULTIPLIER = -1.0;
    private static final double HIKER_PENALTY_WEIGHT = 0.1;

    private final double altitude;

    public MountainAir(final AirInput airInput) {
        super(airInput);
        this.altitude = airInput.getAltitude();
    }

    /**
     * For Mountain Air, it adds the "altitude".
     *
     * @param node The ObjectNode to update.
     */
    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("altitude", altitude);
    }

    /**
     * Calculates the air quality score  for Mountain Air.
     *
     * @return The normalized and rounded quality score.
     */
    @Override
    public double calculateQuality() {
        double oxygenFactor = oxygenLevel
                - (altitude / ALTITUDE_DIVISOR * ALTITUDE_PENALTY_WEIGHT);
        double score = (oxygenFactor * OXYGEN_FACTOR_WEIGHT)
                + (humidity * HUMIDITY_WEIGHT);
        return round(normalize(score + weatherInfluence));
    }

    /**
     * Returns the maximum score for Mountain Air.
     *
     * @return The max score (78 in this case).
     */
    @Override
    public double maxScore() {
        return MAX_MOUNTAIN_SCORE;
    }

    /**
     * Applies a penalty based on the number of hikers if there is a hiking event.
     *
     * @param command The command containing weather details.
     * @return The value to add to the quality score (negative in this case).
     */
    @Override
    public double calculateWeather(final CommandInput command) {
        if (command.getNumberOfHikers() != 0) {
            return NEGATIVE_MULTIPLIER * command.getNumberOfHikers() * HIKER_PENALTY_WEIGHT;
        }
        return 0;
    }
}
