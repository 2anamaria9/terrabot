package main.entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import fileio.CommandInput;
import lombok.Getter;

@Getter
public final class DesertAir extends Air {
    private static final double DUST_PENALTY_WEIGHT = 0.2;
    private static final double TEMP_PENALTY_WEIGHT = 0.3;
    private static final double MAX_SCORE = 65.0;
    private static final double STORM_PENALTY = -30.0;
    private static final double OXYGEN_WEIGHT = 2.0;

    private final double dustParticles;

    public DesertAir(final AirInput airInput) {
        super(airInput);
        this.dustParticles = airInput.getDustParticles();
    }

    /**
     * For Desert Air, it adds the "desertStorm" status.
     *
     * @param node The ObjectNode to update.
     */
    @Override
    public void addSpecificFields(final ObjectNode node) {
        if (this.weatherDuration > 0) {
            node.put("desertStorm", true);
        } else {
            node.put("desertStorm", false);
        }
    }

    /**
     * Calculates the air quality score for Desert Air.
     *
     * @return The normalized and rounded quality score.
     */
    @Override
    public double calculateQuality() {
        double score = (oxygenLevel * OXYGEN_WEIGHT)
                - (dustParticles * DUST_PENALTY_WEIGHT)
                - (temperature * TEMP_PENALTY_WEIGHT);
        return round(normalize(score + weatherInfluence));
    }

    /**
     * Returns the maximum score for Desert Air.
     *
     * @return The max score (65 in this case).
     */
    @Override
    public double maxScore() {
        return MAX_SCORE;
    }

    /**
     * Applies a penalty if a Desert Storm is active.
     *
     * @param command The command containing weather details.
     * @return The value to add to the quality score (negative in this case).
     */
    @Override
    public double calculateWeather(final CommandInput command) {
        if (command.isDesertStorm()) {
            return STORM_PENALTY;
        }
        return 0;
    }
}
