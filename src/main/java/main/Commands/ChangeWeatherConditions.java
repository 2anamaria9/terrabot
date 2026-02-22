package main.Commands;

import fileio.CommandInput;
import main.SimulationMap;
import main.entities.Air.Air;

public final class ChangeWeatherConditions {
    private ChangeWeatherConditions() {

    }
    /**
     * Executes the weather change logic based on the input command.
     * Iterates through the entire map, checks if the Air entity at each cell
     * is affected by the specific weather command, and applies the effect if so.
     *
     * @param command The command input containing weather details.
     * @param map The simulation map.
     * @return A string message indicating success or the specific error.
     */
    public static String execute(final CommandInput command,
                                 final SimulationMap map) {
        int change = 0;
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Air air = map.getCell(x, y).getAir();
                if (air != null) {
                    double value = air.calculateWeather(command);
                    if (value != 0) {
                        air.setWeather(value, 2);
                        change = 1;
                    }
                }
            }
        }
        if (change == 0) {
            return "ERROR: The weather change does not affect the environment."
                    + " Cannot perform action";
        }
        return "The weather has changed.";
    }
}
