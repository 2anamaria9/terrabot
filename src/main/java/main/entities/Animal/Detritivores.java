package main.entities.Animal;

import fileio.AnimalInput;

public final class Detritivores extends Animal {
    private static final double MAX_PERCENTAGE = 100.0;
    private static final double ATTACK_POSSIBILITY = 90.0;
    private static final double DIVISOR = 10.0;

    public Detritivores(final AnimalInput animalInput) {
        super(animalInput);
    }

    /**
     * Calculates the probability of Detrivore animal to attack.
     *
     * @return The calculated probability score.
     */
    @Override
    public double calculateProbability() {
        return (MAX_PERCENTAGE - ATTACK_POSSIBILITY) / DIVISOR;
    }

    /**
     * Checks if the animal is a carnivore or parasite.
     * Used for feeding and moving logic.
     *
     * @return false, because is Detritivore.
     */
    @Override
    public boolean isCarnivoreOrParasite() {
        return false;
    }
}
