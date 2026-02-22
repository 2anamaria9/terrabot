package main.Commands;

import main.Cell;
import main.SimulationMap;
import main.TerraBot;

public final class MoveRobot {
    private static final int NUM_DIRECTIONS = 4;
    private MoveRobot() {

    }
    /**
     * Executes the movement logic for the robot.
     * Scans all valid neighbors, calculates a cost score based on environmental entities
     * and moves the robot to the position with the lowest score.
     *
     * @param map The simulation map.
     * @param robot The robot to be moved.
     * @return A string message indicating the success or the specific error.
     */
    public static String execute(final SimulationMap map,
                                 final TerraBot robot) {
        int[] a = {0, 1, 0, -1};
        int[] b = {1, 0, -1, 0};
        int min = Integer.MAX_VALUE;
        int x = -1;
        int y = -1;
        for (int i = 0; i < NUM_DIRECTIONS; i++) {
            int nextX = robot.getX() + a[i];
            int nextY = robot.getY() + b[i];
            if (nextX >= 0 && nextY >= 0 && nextX < map.getWidth() && nextY < map.getHeight()) {
                Cell cell = map.getCell(nextX, nextY);
                double sum = 0;
                double cnt = 0;
                if (cell.getSoil() != null) {
                    sum += cell.getSoil().calculateProbability();
                    cnt++;
                }
                if (cell.getAir() != null) {
                    sum += cell.getAir().calculateToxicity();
                    cnt++;
                }
                if (cell.getPlant() != null) {
                    sum += cell.getPlant().calculateProbability();
                    cnt++;
                }
                if (cell.getAnimal() != null) {
                    sum += cell.getAnimal().calculateProbability();
                    cnt++;
                }
                double mean = Math.abs(sum / cnt);
                int result = (int) Math.round(mean);
                if (result < min) {
                    min = result;
                    x = nextX;
                    y = nextY;
                }
            }
        }
        if (robot.getEnergy() < min) {
            return "ERROR: Not enough battery left. Cannot perform action";
        }
        robot.move(x, y);
        robot.consumeEnergy(min);
        return "The robot has successfully moved to position (" + x + ", " + y + ").";
    }
}
