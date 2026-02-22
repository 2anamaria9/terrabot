import com.fasterxml.jackson.annotation.JsonProperty;

public class DevmindErrorResult extends DevmindResult {
    @JsonProperty
    private final String error;

    public DevmindErrorResult(String test, Object input, Object expected, Object actual, int testScore, String error) {
        super(test, input, expected, actual, "ERROR", testScore);
        this.error = error;
    }
}
