package main;

import main.entities.Air.Air;
import main.entities.Soil.Soil;
import main.entities.Plant.Plant;
import main.entities.Water.Water;
import main.entities.Animal.Animal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Cell {
    private int x;
    private int y;
    private Air air;
    private Soil soil;
    private Plant plant;
    private Water water;
    private Animal animal;

    public Cell(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the total number of optional objects present in the cell.
     *
     * @return The count of objects (0 to 3).
     */
    public int getTotalNrOfObjects() {
        int count = 0;
        if (plant != null) {
            count++;
        }
        if (water != null) {
            count++;
        }
        if (animal != null) {
            count++;
        }
        return count;
    }
}
