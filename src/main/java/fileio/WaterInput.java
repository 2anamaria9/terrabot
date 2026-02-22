package fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class WaterInput {
    private String type;
    private String name;
    private double mass;
    private double purity;
    private double salinity;
    private double turbidity;
    private double contaminantIndex;
    private double pH;
    private boolean isFrozen;
    private List<PairInput> sections;
}

