package main.entities.Animal;

import fileio.AnimalInput;
import main.Cell;
import lombok.Getter;
import lombok.Setter;
import main.entities.Entity;
import main.entities.Plant.MaturityLevel;
import main.entities.Plant.Plant;
import main.entities.Water.Water;

/**
 * Abstract class representing an Animal entity in the simulation.
 * Handles state, movement, feeding logic, and interaction with other entities.
 */
@Getter
@Setter
public abstract class Animal implements Entity {
    private static final double WATER_INTAKE_RATE = 0.08;
    private static final double FERTILIZER_HIGH = 0.8;
    private static final double FERTILIZER_MEDIUM = 0.5;
    private static final int MOVEMENT_INTERVAL = 2;

    private String type;
    protected String name;
    protected double mass;
    protected AnimalState state;
    protected boolean isScanned;
    protected double fertilizer;
    protected int roundsSinceScanned;
    private boolean scannedRound;

    public Animal(final AnimalInput animalInput) {
        this.type = animalInput.getType();
        this.name = animalInput.getName();
        this.mass = animalInput.getMass();
        this.state = AnimalState.HUNGRY;
        this.isScanned = false;
        this.fertilizer = 0;
        this.roundsSinceScanned = 0;
        this.scannedRound = false;
    }
    /**
     * Resets the fertilizer amount to 0.
     * Used after the fertilizer has been applied to the soil.
     */
    public void resetFertilizer() {
        fertilizer = 0;
    }
    /**
     * Calculates the probability of this animal for attack.
     * Must be implemented by subclasses.
     *
     * @return The probability score.
     */
    public abstract double calculateProbability();

    /**
     * Checks if the animal is a carnivore or a parasite.
     *
     * @return true if carnivore/parasite, false otherwise.
     */
    public abstract boolean isCarnivoreOrParasite();

    /**
     * Marks the animal as scanned.
     */
    public void scan() {
        isScanned = true;
    }

    /**
     * Sets the animal as eaten.
     */
    public void beEaten() {
        mass = 0;
    }

    /**
     * Checks if the animal is dead.
     *
     * @return true if dead.
     */
    public boolean isDead() {
        return mass <= 0;
    }

    /**
     * Sets the animal state to SICK ,usually because of toxic air.
     */
    public void setSick() {
        state = AnimalState.SICK;
    }

    /**
     * Increases the internal age counter if the animal has been scanned.
     * Used for movement logic.
     */
    public void increaseAge() {
        if (isScanned) {
            this.roundsSinceScanned++;
        }
    }

    /**
     * Determines if the animal should move in the current step.
     *
     * @return true if it is time to move (every 2 rounds).
     */
    public boolean shouldMove() {
        return roundsSinceScanned > 0 && roundsSinceScanned % MOVEMENT_INTERVAL == 0;
    }

    /**
     * Implements the feed Algorithm for the animal based on the current cell's contents.
     * Handles eating plants, drinking water, or preying on other animals.
     * Updates mass, state, and fertilizer.
     *
     * @param cell The cell where the animal is currently located.
     */
    public void feedAlgorithm(final Cell cell) {
        Plant plant = cell.getPlant();
        Water water = cell.getWater();
        Animal prey = cell.getAnimal();
        boolean alimentation = false;

        if (isCarnivoreOrParasite()) {
            if (prey != null && prey != this) {
                mass += prey.getMass();
                prey.beEaten();
                fertilizer = FERTILIZER_MEDIUM;
                alimentation = true;
            }
        }

        if (!alimentation) {
            boolean plantCondition = plant != null
                    && plant.getLevel() != MaturityLevel.DEAD
                    && plant.isScanned();
            boolean waterCondition = water != null && !water.isEmpty() && water.isScanned();

            if (plantCondition && waterCondition) {
                mass = mass + plant.getMass();
                plant.beEaten();
                double waterToDrink = Math.min(mass * WATER_INTAKE_RATE, water.getMass());
                mass += waterToDrink;
                water.decreaseMass(waterToDrink);
                fertilizer = FERTILIZER_HIGH;
                alimentation = true;
            } else if (plantCondition) {
                mass += plant.getMass();
                plant.beEaten();
                fertilizer = FERTILIZER_MEDIUM;
                alimentation = true;
            } else if (waterCondition) {
                double waterToDrink = Math.min(mass * WATER_INTAKE_RATE, water.getMass());
                mass += waterToDrink;
                water.decreaseMass(waterToDrink);
                fertilizer = FERTILIZER_MEDIUM;
                alimentation = true;
            }
        }

        if (alimentation) {
            if (state != AnimalState.SICK) {
                state = AnimalState.WELL_FED;
            }
        } else {
            state = AnimalState.HUNGRY;
            fertilizer = 0;
        }

        if (state == AnimalState.SICK) {
            fertilizer = 0;
        }
    }
}
