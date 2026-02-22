package main;

import main.entities.Animal.Animal;

import java.util.ArrayList;
import java.util.List;

public final class MoveAnimal {
    private static final int NUM_DIRECTIONS = 4;
    private MoveAnimal() {

    }
    /**
     * Executes the movement logic for a specific animal at a given position.
     * The algorithm scans the 4 neighboring cells and prioritizes movement according to
     * a certain order.
     * If a valid move is found, the animal is moved on the map and the old cell becomes empty.
     *
     * @param map The simulation map.
     * @param x The current x coordinate of the animal.
     * @param y The current y coordinate of the animal.
     * @param animal The animal entity to be moved.
     */
    public static void execute(final SimulationMap map,
                               final int x,
                               final int y,
                               final Animal animal) {
        int[] a = {0, 1, 0, -1};
        int[] b = {1, 0, -1, 0};
        List<Integer> plantAndWater = new ArrayList<>();
        List<Integer> plant = new ArrayList<>();
        List<Integer> water = new ArrayList<>();
        List<Integer> empty = new ArrayList<>();
        for (int i = 0; i < NUM_DIRECTIONS; i++) {
            int nextX = x + a[i];
            int nextY = y + b[i];
            if (nextX >= 0 && nextY >= 0 && nextX < map.getWidth() && nextY < map.getHeight()) {
                Cell cell = map.getCell(nextX, nextY);
                if (cell.getAnimal() == null || animal.isCarnivoreOrParasite()) {
                    if (cell.getPlant() != null && cell.getWater() != null
                            && cell.getPlant().isScanned() && cell.getWater().isScanned()) {
                        plantAndWater.add(i);
                    } else if (cell.getPlant() != null && cell.getPlant().isScanned()) {
                        plant.add(i);
                    } else if (cell.getWater() != null && cell.getWater().isScanned()) {
                        water.add(i);
                    } else {
                        empty.add(i);
                    }
                }
            }
        }
        int dir = -1;
        if (!plantAndWater.isEmpty()) {
            dir = getBestWaterDir(map, plantAndWater, x, y, a, b);
        } else if (!plant.isEmpty()) {
            dir = plant.getFirst();
        } else if (!water.isEmpty()) {
            dir = getBestWaterDir(map, water, x, y, a, b);
        } else if (!empty.isEmpty()) {
            dir = empty.getFirst();
        }
        if (dir != -1) {
            int newX = x + a[dir];
            int newY = y + b[dir];
            map.getCell(newX, newY).setAnimal(animal);
            map.getCell(x, y).setAnimal(null);
        }
    }
    /**
     * Helper method to find the direction with the best water quality among a list of candidates.
     *
     * @param map  The simulation map.
     * @param dirs The list of valid direction indices to check.
     * @param x    The current x-coordinate.
     * @param y    The current y-coordinate.
     * @param a    The array of x-axis offsets.
     * @param b    The array of y-axis offsets.
     * @return The index of the direction pointing to the cell with the best water quality.
     */
    public static int getBestWaterDir(final SimulationMap map,
                                      final List<Integer> dirs,
                                      final int x,
                                      final int y,
                                      final int[] a,
                                      final int[] b) {
        int bestDir = -1;
        double max = Double.MIN_VALUE;
        for (int dir : dirs) {
            Cell cell = map.getCell(x + a[dir], y + b[dir]);
            double quality = cell.getWater().calculateQuality();
            if (quality > max) {
                max = quality;
                bestDir = dir;
            }
        }
        return bestDir;
    }
}
