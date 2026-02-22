package main.entities.Plant;

import fileio.PlantInput;
import lombok.Getter;
import main.entities.Entity;

@Getter
public abstract class Plant implements Entity {
    private static final double OXYGEN_BONUS_YOUNG = 0.2;
    private static final double OXYGEN_BONUS_MATURE = 0.7;
    private static final double OXYGEN_BONUS_OLD = 0.4;
    private static final double OXYGEN_BONUS_NONE = 0.0;
    private static final double GROWTH_THRESHOLD = 1.0;
    private static final double ROUNDING_FACTOR = 100.0;
    private static final double MAX_PERCENTAGE = 100.0;

    private final String type;
    private final String name;
    private double mass;
    private MaturityLevel level;
    private double growthRate;
    private boolean isScanned;

    public Plant(final PlantInput plantInput) {
        this.type = plantInput.getType();
        this.name = plantInput.getName();
        this.mass = plantInput.getMass();
        this.level = MaturityLevel.YOUNG;
        this.growthRate = 0.0;
        this.isScanned = false;
    }

    /**
     * Marks the plant as scanned by the robot.
     */
    public void scan() {
        isScanned = true;
    }

    /**
     * Indicates the plant has been eaten.
     */
    public void beEaten() {
        mass = 0;
    }

    /**
     * Gets the base oxygen production of the plant type.
     * Implemented by subclasses.
     *
     * @return The base oxygen amount.
     */
    public abstract double getOxygen();

    /**
     * Calculates the probability of blocking.
     * Implemented by subclasses.
     *
     * @return The probability score.
     */
    public abstract double calculateProbability();

    /**
     * Calculates the oxygen bonus based on the current maturity level.
     *
     * @return The oxygen bonus amount.
     */
    public double getMaturityOxygenRate() {
        return switch (level) {
            case YOUNG -> OXYGEN_BONUS_YOUNG;
            case MATURE -> OXYGEN_BONUS_MATURE;
            case OLD -> OXYGEN_BONUS_OLD;
            default -> OXYGEN_BONUS_NONE;
        };
    }

    /**
     * Calculates the total oxygen level produced by this plant.
     * Sum of base oxygen and maturity bonus.
     *
     * @return The total oxygen production.
     */
    public double getOxygenLevel() {
        if (level == MaturityLevel.DEAD) {
            return OXYGEN_BONUS_NONE;
        }
        return getMaturityOxygenRate() + getOxygen();
    }

    /**
     * Increases the growth rate of the plant.
     * If the rate exceeds the threshold, the plant advances to the next maturity level.
     *
     * @param value The amount to grow.
     */
    public void grow(final double value) {
        if (level == MaturityLevel.DEAD) {
            return;
        }
        growthRate += value;
        if (growthRate >= GROWTH_THRESHOLD) {
            nextMaturityLevel();
        }
    }

    /**
     * Advances the plant to the next maturity level and resets growth rate.
     */
    public void nextMaturityLevel() {
        switch (level) {
            case YOUNG:
                level = MaturityLevel.MATURE;
                break;
            case MATURE:
                level = MaturityLevel.OLD;
                break;
            case OLD:
                level = MaturityLevel.DEAD;
                break;
            case DEAD:
                break;
            default:
                break;
        }
        growthRate = 0;
    }

    /**
     * Checks if the plant is dead (either by age or by being eaten).
     *
     * @return true if the plant is dead.
     */
    public boolean isDead() {
        return level == MaturityLevel.DEAD || mass <= 0;
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
