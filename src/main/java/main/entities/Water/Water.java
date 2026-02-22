package main.entities.Water;

import fileio.WaterInput;
import lombok.Getter;
import main.entities.Entity;

/**
 * Represents a Water entity in the simulation.
 * Handles water properties and quality calculation.
 */
@Getter
public final class Water implements Entity {
    private static final double MAX_PERCENTAGE = 100.0;
    private static final double NEUTRAL_PH = 7.5;
    private static final double MAX_SALINITY = 350.0;
    private static final double MAX_TURBIDITY = 100.0;
    private static final double MAX_CONTAMINANT = 100.0;
    private static final double PURITY_WEIGHT = 0.3;
    private static final double PH_WEIGHT = 0.2;
    private static final double SALINITY_WEIGHT = 0.15;
    private static final double TURBIDITY_WEIGHT = 0.1;
    private static final double CONTAMINANT_WEIGHT = 0.15;
    private static final double FROZEN_WEIGHT = 0.2;
    private static final double ROUNDING_FACTOR = 100.0;

    private final String type;
    private final String name;
    private double mass;
    private final double salinity;
    private final double pH;
    private final double purity;
    private final double turbidity;
    private final double contaminantIndex;
    private final boolean isFrozen;
    private boolean isScanned;

    public Water(final WaterInput waterInput) {
        this.type = waterInput.getType();
        this.name = waterInput.getName();
        this.mass = waterInput.getMass();
        this.salinity = waterInput.getSalinity();
        this.pH = waterInput.getPH();
        this.purity = waterInput.getPurity();
        this.turbidity = waterInput.getTurbidity();
        this.contaminantIndex = waterInput.getContaminantIndex();
        this.isFrozen = waterInput.isFrozen();
        this.isScanned = false;
    }

    /**
     * Checks if the water source is empty.
     * @return true if mass is 0 or less.
     */
    public boolean isEmpty() {
        return mass <= 0;
    }

    /**
     * Marks the water as scanned by the robot.
     */
    public void scan() {
        isScanned = true;
    }

    /**
     * Calculates the quality score of the water based on its properties.
     * @return the calculated quality score.
     */
    public double calculateQuality() {
        double purityScore = purity / MAX_PERCENTAGE;
        double pHScore = 1 - Math.abs(pH - NEUTRAL_PH) / NEUTRAL_PH;
        double salinityScore = 1 - (salinity / MAX_SALINITY);
        double turbidityScore = 1 - (turbidity / MAX_TURBIDITY);
        double contaminantScore = 1 - (contaminantIndex / MAX_CONTAMINANT);

        int frozenScore = 1;
        if (isFrozen) {
            frozenScore = 0;
        }

        double waterQuality = (PURITY_WEIGHT * purityScore
                + PH_WEIGHT * pHScore
                + SALINITY_WEIGHT * salinityScore
                + TURBIDITY_WEIGHT * turbidityScore
                + CONTAMINANT_WEIGHT * contaminantScore
                + FROZEN_WEIGHT * frozenScore) * MAX_PERCENTAGE;

        return round(waterQuality);
    }

    /**
     * Decreases the mass of the water source.
     * @param value The amount to decrease.
     */
    public void decreaseMass(final double value) {
        mass -= value;
        if (mass < 0) {
            mass = 0;
        }
    }

    /**
     * Rounds a double value.
     * @param value the value to round.
     * @return the rounded value.
     */
    private double round(final double value) {
        return Math.round(value * ROUNDING_FACTOR) / ROUNDING_FACTOR;
    }
}
