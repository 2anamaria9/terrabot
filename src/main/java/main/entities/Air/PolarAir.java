package main.entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import fileio.CommandInput;
import lombok.Getter;

@Getter
public final class PolarAir extends Air {
    private static final double ICE_PENALTY_WEIGHT = 0.05;
    private static final double TEMP_BASE = 100.0;
    private static final double MAX_POLAR_SCORE = 142.0;
    private static final double STORM_PENALTY_WEIGHT = 0.2;
    private static final double OXYGEN_WEIGHT = 2.0;
    private static final double NEGATIVE_MULTIPLIER = -1.0;

    private final double iceCrystalConcentration;

    public PolarAir(final AirInput airInput) {
        super(airInput);
        this.iceCrystalConcentration = airInput.getIceCrystalConcentration();
    }

    /**
     * For Polar Air, it adds the "iceCrystalConcentration".
     *
     * @param node The ObjectNode to update.
     */
    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("iceCrystalConcentration", iceCrystalConcentration);
    }

    /**
     * Calculates the air quality score for Polar Air.
     *
     * @return The normalized and rounded quality score.
     */
    @Override
    public double calculateQuality() {
        double score = (oxygenLevel * OXYGEN_WEIGHT)
                + (TEMP_BASE - Math.abs(temperature))
                - (iceCrystalConcentration * ICE_PENALTY_WEIGHT);
        return round(normalize(score + weatherInfluence));
    }

    /**
     * Returns the maximum score for Polar Air.
     *
     * @return The max score (142 in this case).
     */
    @Override
    public double maxScore() {
        return MAX_POLAR_SCORE;
    }

    /**
     * Applies a penalty based on wind speed if there is a polar storm.
     *
     * @param command The command containing weather details.
     * @return The value to add to the quality score (negative in this case).
     */
    @Override
    public double calculateWeather(final CommandInput command) {
        if (command.getWindSpeed() != 0) {
            return NEGATIVE_MULTIPLIER * command.getWindSpeed() * STORM_PENALTY_WEIGHT;
        }
        return 0;
    }
}
