package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.InputLoader;
import main.Commands.PrintEnvConditions;
import main.Commands.PrintMap;
import main.Commands.MoveRobot;
import main.Commands.ChangeWeatherConditions;
import main.Commands.ScanObject;
import main.Commands.LearnFact;
import main.Commands.PrintKnowledgeBase;
import main.Commands.ImproveEnvironment;
import java.io.File;
import java.io.IOException;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {

    private Main() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();

    /**
     * @param inputPath input file path
     * @param outputPath output file path
     * @throws IOException when files cannot be loaded.
     */
    public static void action(final String inputPath,
                              final String outputPath) throws IOException {

        InputLoader inputLoader = new InputLoader(inputPath);
        ArrayNode output = MAPPER.createArrayNode();
        Simulation sim = null;
        int idx = 0;
        int previousTimestamp = 0;
        for (CommandInput command : inputLoader.getCommands()) {
            String commandName = command.getCommand();
            int currentTimestamp = command.getTimestamp();
            ObjectNode out = MAPPER.createObjectNode();
            out.put("command", commandName);
            if (commandName.equals("startSimulation")) {
                if (sim == null) {
                    sim = new Simulation(inputLoader.getSimulations().get(idx));
                    previousTimestamp = currentTimestamp;
                    out.put("message", "Simulation has started.");
                    idx++;
                } else {
                    out.put("message", "ERROR: Simulation already"
                            + " started. Cannot perform action");
                }
            } else if (commandName.equals("endSimulation")) {
                if (sim == null) {
                    out.put("message", "ERROR: Simulation not "
                            + "started. Cannot perform action");
                } else {
                    sim = null;
                    out.put("message", "Simulation has ended.");
                }
            } else {
                if (sim == null) {
                    out.put("message", "ERROR: Simulation not "
                            + "started. Cannot perform action");
                } else {
                    int dif = currentTimestamp - previousTimestamp;
                    for (int i = 0; i < dif; i++) {
                        sim.updateEnvironment();
                    }
                    if (sim.isCharging(command.getTimestamp())) {
                        out.put("message", "ERROR: Robot still charging."
                                + " Cannot perform action");
                    } else if (commandName.equals("printEnvConditions")) {
                        out.put("output",
                                PrintEnvConditions.execute(MAPPER, sim.getMap(), sim.getRobot()));
                    } else if (commandName.equals("printMap")) {
                        out.put("output", PrintMap.execute(MAPPER, sim.getMap()));
                    } else if (commandName.equals("moveRobot")) {
                        out.put("message", MoveRobot.execute(sim.getMap(), sim.getRobot()));
                    } else if (commandName.equals("getEnergyStatus")) {
                        out.put("message", "TerraBot has " + sim.getRobot().getEnergy()
                                + " energy points left.");
                    } else if (commandName.equals("rechargeBattery")) {
                        sim.getRobot().rechargeEnergy(command.getTimeToCharge());
                        sim.setChargeUntil(currentTimestamp + command.getTimeToCharge());
                        out.put("message", "Robot battery is charging.");
                    } else if (commandName.equals("changeWeatherConditions")) {
                        out.put("message",
                                ChangeWeatherConditions.execute(command, sim.getMap()));
                    } else if (commandName.equals("scanObject")) {
                        out.put("message",
                                ScanObject.execute(command, sim.getMap(), sim.getRobot()));
                    } else if (commandName.equals("learnFact")) {
                        out.put("message", LearnFact.execute(command, sim.getRobot()));
                    } else if (commandName.equals("printKnowledgeBase")) {
                        out.put("output", PrintKnowledgeBase.execute(MAPPER, sim.getRobot()));
                    } else if (commandName.equals("improveEnvironment")) {
                        out.put("message",
                                ImproveEnvironment.execute(command, sim.getMap(), sim.getRobot()));
                    }
                }
            }
            if (currentTimestamp != 0) {
                out.put("timestamp", currentTimestamp);
                previousTimestamp = currentTimestamp;
            }
            output.add(out);
        }

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        WRITER.writeValue(outputFile, output);
    }
}
