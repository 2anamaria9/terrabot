package main.entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import fileio.CommandInput;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public final class TropicalAir extends Air {
    private static final double HUMIDITY_WEIGHT = 0.5;
    private static final double CO2_PENALTY_WEIGHT = 0.01;
    private static final double MAX_TROPICAL_SCORE = 82.0;
    private static final double RAIN_BONUS_MULTIPLIER = 0.3;
    private static final double OXYGEN_WEIGHT = 2.0;
    private static final int ROUNDING_SCALE = 2;

    private double co2Level;

    public TropicalAir(final AirInput airInput) {
        super(airInput);
        this.co2Level = airInput.getCo2Level();
    }

    /**
     * For Tropical Air, it adds the "co2Level".
     * Before the addition, the co2Level is rounded to two decimal places.
     *
     * @param node The ObjectNode to update.
     */
    @Override
    public void addSpecificFields(final ObjectNode node) {
        BigDecimal bd = new BigDecimal(co2Level);
        bd = bd.setScale(ROUNDING_SCALE, BigDecimal.ROUND_HALF_UP);
        co2Level = bd.doubleValue();
        node.put("co2Level", co2Level);
    }

    /**
     * Calculates the air quality score for Tropical Air.
     *
     * @return The normalized and rounded quality score.
     */
    @Override
    public double calculateQuality() {
        double score = (oxygenLevel * OXYGEN_WEIGHT)
                + (humidity * HUMIDITY_WEIGHT)
                - (co2Level * CO2_PENALTY_WEIGHT);
        return round(normalize(score + weatherInfluence));
    }

    /**
     * Returns the maximum score for this air type.
     *
     * @return The max score (82 in this case).
     */
    @Override
    public double maxScore() {
        return MAX_TROPICAL_SCORE;
    }

    /**
     * Applies a bonus if there is rainfall.
     *
     * @param command The command containing weather details.
     * @return The value to add to the quality score (positive in this case).
     */
    @Override
    public double calculateWeather(final CommandInput command) {
        if (command.getRainfall() != 0) {
            return command.getRainfall() * RAIN_BONUS_MULTIPLIER;
        }
        return 0.0;
    }
}
