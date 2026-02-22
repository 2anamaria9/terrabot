package main.Commands;

import fileio.CommandInput;
import main.Cell;
import main.SimulationMap;
import main.TerraBot;

public final class ImproveEnvironment {
    private static final int ENERGY_COST = 10;
    private static final double OXYGEN_BONUS = 0.3;
    private static final double ORGANIC_MATTER_BONUS = 0.3;
    private static final double HUMIDITY_BONUS = 0.2;
    private static final double WATER_RETENTION_BONUS = 0.2;
    private ImproveEnvironment() {

    }
    /**
     * Executes the environment improvement logic.
     * Checks for sufficient energy, inventory item existence and knowledge of the required fact.
     * If valid, applies the improvement to the current cell and consumes resources.
     *
     * @param command The command input containing details.
     * @param map The simulation map.
     * @param robot The robot performing the action.
     * @return A string message indicating the success or the specific error.
     */
    public static String execute(final CommandInput command,
                                 final SimulationMap map,
                                 final TerraBot robot) {
        if (robot.getEnergy() < ENERGY_COST) {
            return "ERROR: Not enough battery left. Cannot perform action";
        }
        String type = command.getImprovementType();
        String entity = command.getName();
        String fact = null;
        switch (type) {
            case "plantVegetation":
                fact = "Method to plant " + entity;
                break;
            case "fertilizeSoil":
                fact = "Method to fertilize with " + entity;
                break;
            case "increaseHumidity":
                fact = "Method to increase humidity";
                break;
            case "increaseMoisture":
                fact = "Method to increaseMoisture";
                break;
            default:
                break;
        }
        if (!robot.hasEntity(entity)) {
            return "ERROR: Subject not yet saved. Cannot perform action";
        }
        if (!robot.knowsFact(entity, fact)) {
            return "ERROR: Fact not yet saved. Cannot perform action";
        }
        Cell cell = map.getCell(robot.getX(), robot.getY());
        String output = null;
        switch (type) {
            case "plantVegetation":
                if (cell.getAir() != null) {
                    cell.getAir().updateOxygen(OXYGEN_BONUS);
                    output = "The " + entity + " was planted successfully.";
                }
                break;
            case "fertilizeSoil":
                if (cell.getSoil() != null) {
                    cell.getSoil().updateOrganicMatter(ORGANIC_MATTER_BONUS);
                    output = "The soil was successfully fertilized using " + entity;
                }
                break;
            case "increaseHumidity":
                if (cell.getAir() != null) {
                    cell.getAir().updateHumidity(HUMIDITY_BONUS);
                    output = "The humidity was successfully increased using " + entity;
                }
                break;
            case "increaseMoisture":
                if (cell.getSoil() != null) {
                    cell.getSoil().updateWaterRetention(WATER_RETENTION_BONUS);
                    output = "The moisture was successfully increased using " + entity;
                }
                break;
            default:
                break;
        }
        robot.consumeEnergy(ENERGY_COST);
        robot.removeFromInventory(entity);
        return output;
    }
}
