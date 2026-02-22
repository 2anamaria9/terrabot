package fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class AirInput {
    private String type;
    private String name;
    private double mass;
    private double humidity;
    private double temperature;
    private double oxygenLevel;
    private double altitude;
    private double pollenLevel;
    private double co2Level;
    private double iceCrystalConcentration;
    private double dustParticles;
    private List<PairInput> sections;
}

