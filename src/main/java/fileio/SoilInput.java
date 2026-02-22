package fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SoilInput {
    private String type;
    private String name;
    private double mass;
    private double nitrogen;
    private double waterRetention;
    private double soilpH;
    private double organicMatter;
    private double leafLitter;
    private double waterLogging;
    private double permafrostDepth;
    private double rootDensity;
    private double salinity;
    private List<PairInput> sections;
}

