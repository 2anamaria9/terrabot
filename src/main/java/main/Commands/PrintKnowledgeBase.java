package main.Commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.TerraBot;

import java.util.List;
import java.util.Map;

public final class PrintKnowledgeBase {
    private PrintKnowledgeBase() {

    }
    /**
     * Executes the logic to print the robot's knowledge base.
     * Iterates through the robot's database and constructs a JSON ArrayNode containing
     * all learned information.
     *
     * @param mapper The ObjectMapper used to create JSON nodes.
     * @param robot The robot containing the knowledge base.
     * @return An ArrayNode containing the list of topics and facts.
     */
    public static ArrayNode execute(final ObjectMapper mapper,
                                    final TerraBot robot) {
        ArrayNode output = mapper.createArrayNode();
        Map<String, List<String>> dataBase = robot.getDataBase();
        for (Map.Entry<String, List<String>> entry : dataBase.entrySet()) {
            ObjectNode node = mapper.createObjectNode();
            node.put("topic", entry.getKey());
            ArrayNode facts = mapper.createArrayNode();
            for (String fact : entry.getValue()) {
                facts.add(fact);
            }
            node.set("facts", facts);
            output.add(node);
        }
        return output;
    }
}
