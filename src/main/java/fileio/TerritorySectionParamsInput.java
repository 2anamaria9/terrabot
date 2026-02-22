package fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TerritorySectionParamsInput {
    private List<SoilInput> soil;
    private List<PlantInput> plants;
    private List<AnimalInput> animals;
    private List<WaterInput> water;
    private List<AirInput> air;
}


