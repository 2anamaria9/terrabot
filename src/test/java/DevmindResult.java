import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

public class DevmindResult {
    @JsonProperty
    private final String test;

    @JsonProperty
    private final Object input;

    @JsonProperty
    private final Object expected;

    @JsonProperty
    private final Object actual;

    @JsonProperty
    private final String status;

    @JsonProperty("test_score")
    private final int testScore;


    public DevmindResult(String test, Object input, Object expected, Object actual, String status, int testScore) {
        this.test = test;
        this.input = input;
        this.expected = expected;
        this.actual = actual;
        this.status = status;
        this.testScore = testScore;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    public String getStatus() {
        return status;
    }
}
