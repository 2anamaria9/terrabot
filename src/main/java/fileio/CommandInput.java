package fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CommandInput {
    private String command;
    private String smell;
    private String color;
    private String sound;
    private int timestamp;
    private int timeToCharge;
    private String direction;
    private String subject;
    private String type;
    private String components;
    private double rainfall;
    private double windSpeed;
    private boolean desertStorm;
    private String season;
    private int numberOfHikers;
    private String improvementType;
    private String name;
}

