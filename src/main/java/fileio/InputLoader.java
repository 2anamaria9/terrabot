package fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic skeleton for loading input JSON file as a Map.
 * Students should implement deeper parsing themselves.
 */
@Getter
public final class InputLoader {
    private final ArrayList<SimulationInput> simulations;
    private final ArrayList<CommandInput> commands;

    public InputLoader(final String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputRoot root = mapper.readValue(new File(filePath), InputRoot.class);
        this.simulations = new ArrayList<>(root.simulationParams);
        this.commands = new ArrayList<>(root.commands);
    }

    // Helper class for root deserialization
    @Data
    @NoArgsConstructor
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static final class InputRoot {
        private List<SimulationInput> simulationParams;
        private List<CommandInput> commands;
    }
}
