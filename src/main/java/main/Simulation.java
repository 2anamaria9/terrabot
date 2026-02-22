package main;
import fileio.AnimalInput;
import fileio.PlantInput;
import fileio.SoilInput;
import fileio.WaterInput;
import fileio.AirInput;
import fileio.PairInput;
import fileio.TerritorySectionParamsInput;
import fileio.SimulationInput;
import main.entities.Air.Air;
import main.entities.Water.Water;
import main.entities.Soil.Soil;
import main.entities.Plant.Plant;
import main.entities.Animal.Animal;
import main.entities.Soil.ForestSoil;
import main.entities.Soil.GrasslandSoil;
import main.entities.Soil.DesertSoil;
import main.entities.Soil.SwampSoil;
import main.entities.Soil.TundraSoil;
import main.entities.Plant.FloweringPlants;
import main.entities.Plant.GymnospermsPlants;
import main.entities.Plant.Ferns;
import main.entities.Plant.Mosses;
import main.entities.Plant.Algae;
import main.entities.Animal.Herbivores;
import main.entities.Animal.Carnivores;
import main.entities.Animal.Omnivores;
import main.entities.Animal.Detritivores;
import main.entities.Animal.Parasites;
import main.entities.Air.TropicalAir;
import main.entities.Air.MountainAir;
import main.entities.Air.PolarAir;
import main.entities.Air.TemperateAir;
import main.entities.Air.DesertAir;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

@Getter
public final class Simulation {
    private static final double HUMIDITY_UPDATE_VAL = 0.1;
    private static final double WATER_RETENTION_UPDATE_VAL = 0.1;
    private static final double PLANT_GROWTH_VAL = 0.2;

    private SimulationMap map;
    private TerraBot robot;
    private ObjectMapper mapper = new ObjectMapper();
    private int totalTime = 0;
    private int step = 0;
    public Simulation(final SimulationInput input) {
        this.robot = new TerraBot(input.getEnergyPoints());
        String[] dims = input.getTerritoryDim().split("x");
        int height = Integer.parseInt(dims[0]);
        int width = Integer.parseInt(dims[1]);
        this.map = new SimulationMap(width, height);
        populateMap(input.getTerritorySectionParams());
    }
    /**
     * Populates the simulation map with entities based on the provided input parameters.
     * This method iterates through lists of input data and for each entity it instantiates
     * the specific subclass.
     *
     * @param params The input object containing lists of parameters for all
     * territory sections.
     */
    private void populateMap(final TerritorySectionParamsInput params) {
        for (SoilInput input : params.getSoil()) {
            for (PairInput p : input.getSections()) {
                Soil o = null;
                switch (input.getType()) {
                    case "ForestSoil":
                        o = new ForestSoil(input);
                        break;
                    case "SwampSoil":
                        o = new SwampSoil(input);
                        break;
                    case "DesertSoil":
                        o = new DesertSoil(input);
                        break;
                    case "GrasslandSoil":
                        o = new GrasslandSoil(input);
                        break;
                    case "TundraSoil":
                        o = new TundraSoil(input);
                        break;
                    default:
                        break;
                }
                map.getCell(p.getX(), p.getY()).setSoil(o);
            }
        }
        for (PlantInput input : params.getPlants()) {
            for (PairInput p : input.getSections()) {
                Plant o = null;
                switch (input.getType()) {
                    case "FloweringPlants":
                        o = new FloweringPlants(input);
                        break;
                    case "GymnospermsPlants":
                        o = new GymnospermsPlants(input);
                        break;
                    case "Ferns":
                        o = new Ferns(input);
                        break;
                    case "Mosses":
                        o = new Mosses(input);
                        break;
                    case "Algae":
                        o = new Algae(input);
                        break;
                    default:
                        break;
                }
                map.getCell(p.getX(), p.getY()).setPlant(o);
            }
        }
        for (AnimalInput input : params.getAnimals()) {
            for (PairInput p : input.getSections()) {
                Animal o = null;
                switch (input.getType()) {
                    case "Herbivores":
                        o = new Herbivores(input);
                        break;
                    case "Carnivores":
                        o = new Carnivores(input);
                        break;
                    case "Omnivores":
                        o = new Omnivores(input);
                        break;
                    case "Detritivores":
                        o = new Detritivores(input);
                        break;
                    case "Parasites":
                        o = new Parasites(input);
                        break;
                    default:
                        break;
                }
                map.getCell(p.getX(), p.getY()).setAnimal(o);
            }
        }
        for (WaterInput input : params.getWater()) {
            for (PairInput p : input.getSections()) {
                Water o = new Water(input);
                map.getCell(p.getX(), p.getY()).setWater(o);
            }
        }
        for (AirInput input : params.getAir()) {
            for (PairInput p : input.getSections()) {
                Air o = null;
                switch (input.getType()) {
                    case "TropicalAir":
                        o = new TropicalAir(input);
                        break;
                    case "PolarAir":
                        o = new PolarAir(input);
                        break;
                    case "TemperateAir":
                        o = new TemperateAir(input);
                        break;
                    case "DesertAir":
                        o = new DesertAir(input);
                        break;
                    case "MountainAir":
                        o = new MountainAir(input);
                        break;
                    default:
                        break;
                }
                map.getCell(p.getX(), p.getY()).setAir(o);
            }
        }
    }
    /**
     * Sets the timestamp until the robot is busy charging.
     *
     * @param timestamp The timestamp when charging finishes.
     */
    public void setChargeUntil(final int timestamp) {
        this.totalTime = timestamp;
    }
    /**
     * Checks if the robot is currently charging.
     *
     * @param timestamp The current timestamp.
     * @return true if the robot is charging, false otherwise.
     */
    public boolean isCharging(final int timestamp) {
        return timestamp < totalTime;
    }
    /**
     * Updates the environment state for one simulation step.
     * Processes interactions between entities.
     */
    public void updateEnvironment() {
        step++;
        Set<Animal> movedAnimals = new HashSet<>();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                Air air = cell.getAir();
                Soil soil = cell.getSoil();
                Animal animal = cell.getAnimal();
                Plant plant = cell.getPlant();
                Water water = cell.getWater();
                // Decrease the duration of active weather events
                if (air != null) {
                    air.updateWeatherDuration();
                }
                // Delete dead/empty entities from the map
                if (plant != null && plant.isDead()) {
                    cell.setPlant(null);
                }
                if (water != null && water.getMass() <= 0) {
                    cell.setWater(null);
                }
                if (animal != null && animal.isDead()) {
                    cell.setAnimal(null);
                }
                // The influence of water on the environment
                if (water != null && air != null && water.isScanned()) {
                    if (step % 2 == 0) {
                        air.updateHumidity(HUMIDITY_UPDATE_VAL);
                    }
                }
                if (water != null && soil != null && water.isScanned()) {
                    if (step % 2 == 0) {
                        soil.updateWaterRetention(WATER_RETENTION_UPDATE_VAL);
                    }
                }
                // The plant growth
                if (soil != null && plant != null && plant.isScanned()) {
                    plant.grow(PLANT_GROWTH_VAL);
                }
                if (water != null && plant != null && plant.isScanned()) {
                    plant.grow(PLANT_GROWTH_VAL);
                }
                // Oxygen production
                if (plant != null && air != null && plant.isScanned()) {
                    air.updateOxygen(plant.getOxygenLevel());
                }
                // Animal logic
                // isScannedRound prevents double logical processing in the same iteration
                if (animal != null && animal.isScanned() && !animal.isScannedRound()) {
                    animal.increaseAge();
                    animal.setScannedRound(true);
                    if (!movedAnimals.contains(animal)) {
                        if (air != null) {
                            if (air.checkToxicity()) {
                                animal.setSick();
                            }
                        }
                        animal.feedAlgorithm(cell);
                        double fert = animal.getFertilizer();
                        if (fert > 0 && soil != null) {
                            soil.updateOrganicMatter(fert);
                            animal.resetFertilizer();
                        }
                        if (animal.shouldMove() && !animal.isDead()) {
                            MoveAnimal.execute(map, x, y, animal);
                            movedAnimals.add(animal);
                        }
                    }
                }
            }
        }
        // Reset the 'scannedRound' flag for all animals on the map
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                Animal animal = cell.getAnimal();
                if (animal != null) {
                    animal.setScannedRound(false);
                }
            }
        }
    }
}
