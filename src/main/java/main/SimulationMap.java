package main;

import lombok.Getter;

@Getter
public final class SimulationMap {
    private final int width;
    private final int height;
    private final Cell[][] cells;

    public SimulationMap(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    /**
     * Returns a cell at a specific coordinate.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The Cell object at the specified coordinates, or null if out of bounds.
     */
    public Cell getCell(final int x, final int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return cells[x][y];
    }
}
