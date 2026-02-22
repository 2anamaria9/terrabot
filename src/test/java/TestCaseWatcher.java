import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Arrays;

public class TestCaseWatcher implements TestWatcher {
    public record TestCaseData(String testName, String input,  String output, String ref, int points) { }

    public static final StringBuilder stringBuilder = new StringBuilder();

    public static int totalPoints = 0;

    @Override
    public void testSuccessful(ExtensionContext context) {
        try {
            TestCaseData testCaseData = getTestCaseData(context);
            stringBuilder.append("CASE - %s - %s/%s = PASSED\n".formatted(testCaseData.testName(), testCaseData.points()));
            totalPoints += testCaseData.points();
        }
        catch (IllegalArgumentException e) {
            // Do nothing
        }
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        try {
            TestCaseData testCaseData = getTestCaseData(context);
            stringBuilder.append("CASE - %s - 0/%s = FAILED\n".formatted(testCaseData.testName(), testCaseData.points(), testCaseData.points()));
        }
        catch (IllegalArgumentException e) {
            // Do nothing
        }
    }

    private TestCaseData getTestCaseData(ExtensionContext context) {
        String[] testCaseSplit = context.getDisplayName().split(",");

        if (testCaseSplit.length != 5) {
            throw new IllegalArgumentException();
        }

        String[] data = Arrays.stream(testCaseSplit)
                .map(String::strip)
                .toArray(String[]::new);

        return new TestCaseData(data[0], data[1], data[2], data[3], Integer.parseInt(data[4]));
    }
}
