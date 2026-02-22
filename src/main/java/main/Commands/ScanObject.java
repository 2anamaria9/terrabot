package main.Commands;

import fileio.CommandInput;
import main.Cell;
import main.SimulationMap;
import main.TerraBot;
import main.entities.Entity;

public final class ScanObject {
    private static final int ENERGY_COST = 7;
    private ScanObject() {

    }
    /**
     * Executes the scanning logic based on sensory inputs.
     * If the robot has enough energy, detects sensory data from the command, identifies the
     * type of object
     * If the object exists in the current cell, it is marked as scanned, added to the
     * robot's inventory and energy is consumed.
     *
     * @param command The command input containing sensory data.
     * @param map The simulation map.
     * @param robot The robot performing the scan.
     * @return A string message indicating the result of the scan or the specific error.
     */
    public static String execute(final CommandInput command,
                                 final SimulationMap map,
                                 final TerraBot robot) {
        if (robot.getEnergy() < ENERGY_COST) {
            return "ERROR: Not enough energy to perform action";
        }
        String color = command.getColor();
        String smell = command.getSmell();
        String sound = command.getSound();
        Cell cell = map.getCell(robot.getX(), robot.getY());
        String objectType = null;
        Entity entity = null;
        if (color.equals("none") && smell.equals("none") && sound.equals("none")) {
            if (cell.getWater() != null) {
                cell.getWater().scan();
                objectType = "water.";
                entity = cell.getWater();
            }
        } else if (sound.equals("none")) {
            if (cell.getPlant() != null) {
                cell.getPlant().scan();
                objectType = "a plant.";
                entity = cell.getPlant();
            }
        } else if (cell.getAnimal() != null) {
            cell.getAnimal().scan();
            objectType = "an animal.";
            entity = cell.getAnimal();
        }
        if (objectType == null) {
            return "ERROR: Object not found. Cannot perform action";
        } else {
            robot.consumeEnergy(ENERGY_COST);
            robot.addToInventory(entity);
            return "The scanned object is " + objectType;
        }
    }
}
