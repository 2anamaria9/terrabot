package main.Commands;

import fileio.CommandInput;
import main.TerraBot;

public final class LearnFact {
    private LearnFact() {

    }
    /**
     * Executes the logic for learning a new fact.
     * If the robot has enough energy checks if the target entity exists
     * in the robot's inventory.
     * If successful, the energy will consume and the fact will be saved in database.
     *
     * @param command The command input containing details.
     * @param robot The robot performing the action.
     * @return A string message indicating the success or the specific error.
     */
    public static String execute(final CommandInput command,
                                 final TerraBot robot) {
        if (robot.getEnergy() < 2) {
            return "ERROR: Not enough battery left. Cannot perform action";
        }
        String entity = command.getComponents();
        String fact = command.getSubject();
        if (!robot.hasEntity(entity)) {
            return "ERROR: Subject not yet saved. Cannot perform action";
        }
        robot.consumeEnergy(2);
        robot.addToDataBase(entity, fact);
        return "The fact has been successfully saved in the database.";
    }
}
