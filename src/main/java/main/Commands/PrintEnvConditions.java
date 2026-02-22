package main.Commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.Cell;
import main.SimulationMap;
import main.TerraBot;

public final class PrintEnvConditions {
    private PrintEnvConditions() {

    }
    /**
     * Executes the logic to print environmental conditions.
     * Creates a JSON object containing information about all entities
     * that exist on robot's current coordinates.
     *
     * @param mapper The ObjectMapper used to create JSON nodes.
     * @param map The simulation map.
     * @param robot The robot used to determine the current cell.
     * @return An ObjectNode containing the structured JSON data of the environment conditions.
     */
    public static ObjectNode execute(final ObjectMapper mapper,
                                     final SimulationMap map,
                                     final TerraBot robot) {
        ObjectNode envConditions = mapper.createObjectNode();
        Cell cell = map.getCell(robot.getX(), robot.getY());
        if (cell.getSoil() != null) {
            ObjectNode node = mapper.createObjectNode();
            node.put("type", cell.getSoil().getType());
            node.put("name", cell.getSoil().getName());
            node.put("mass", cell.getSoil().getMass());
            node.put("nitrogen", cell.getSoil().getNitrogen());
            node.put("waterRetention", cell.getSoil().getWaterRetention());
            node.put("soilpH", cell.getSoil().getSoilpH());
            node.put("organicMatter", cell.getSoil().getOrganicMatter());
            node.put("soilQuality", cell.getSoil().calculateQuality());
            cell.getSoil().addSpecificFields(node);
            envConditions.set("soil", node);
        }
        if (cell.getPlant() != null) {
            ObjectNode node = mapper.createObjectNode();
            node.put("type", cell.getPlant().getType());
            node.put("name", cell.getPlant().getName());
            node.put("mass", cell.getPlant().getMass());
            envConditions.set("plants", node);
        }
        if (cell.getAnimal() != null) {
            ObjectNode node = mapper.createObjectNode();
            node.put("type", cell.getAnimal().getType());
            node.put("name", cell.getAnimal().getName());
            node.put("mass", cell.getAnimal().getMass());
            envConditions.set("animals", node);
        }
        if (cell.getWater() != null) {
            ObjectNode node = mapper.createObjectNode();
            node.put("type", cell.getWater().getType());
            node.put("name", cell.getWater().getName());
            node.put("mass", cell.getWater().getMass());
            envConditions.set("water", node);
        }
        if (cell.getAir() != null) {
            ObjectNode node = mapper.createObjectNode();
            node.put("type", cell.getAir().getType());
            node.put("name", cell.getAir().getName());
            node.put("mass", cell.getAir().getMass());
            node.put("humidity", cell.getAir().getHumidity());
            node.put("temperature", cell.getAir().getTemperature());
            node.put("oxygenLevel", cell.getAir().getOxygenLevel());
            node.put("airQuality", cell.getAir().calculateQuality());
            cell.getAir().addSpecificFields(node);
            envConditions.set("air", node);
        }
        return envConditions;
    }
}
