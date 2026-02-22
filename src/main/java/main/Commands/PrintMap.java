package main.Commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.Cell;
import main.SimulationMap;

public final class PrintMap {
    private PrintMap() {

    }
    /**
     * Executes the logic to generate the map details.
     * Iterates through every cell in the simulation grid and creates a JSON object
     * for each, containing coordinates ,number of objects, air quality and
     * soil quality
     *
     * @param mapper The ObjectMapper used to create JSON nodes.
     * @param map The simulation map.
     * @return A JsonNode containing the list of all cell states.
     */
    public static JsonNode execute(final ObjectMapper mapper,
                                   final SimulationMap map) {
        ArrayNode arrayNode = mapper.createArrayNode();
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                ObjectNode node = mapper.createObjectNode();
                Cell cell = map.getCell(j, i);
                ArrayNode coords = mapper.createArrayNode();
                coords.add(j);
                coords.add(i);
                node.set("section", coords);
                node.put("totalNrOfObjects", cell.getTotalNrOfObjects());
                node.put("airQuality", cell.getAir().interpretationQuality());
                node.put("soilQuality", cell.getSoil().interpretationQuality());
                arrayNode.add(node);
            }
        }
        return arrayNode;
    }
}
