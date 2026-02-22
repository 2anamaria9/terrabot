import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import main.Main;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.*;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@ExtendWith(TestCaseWatcher.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestRunner {
    private static final ObjectMapper objectMapper = new ObjectMapper(
            new JsonFactory().enable(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION)
    );

    private static final List<DevmindResult> devmindResults = new ArrayList<>();

    private static final String PASSED = "PASSED";

    public static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("test01", "input/test01_initialize_entities.json", "out/out_test01_initialize_entities.json", "ref/ref_test01_initialize_entities.json", 3),
                Arguments.of("test02", "input/test02_initialize_entities_errors.json", "out/out_test02_initialize_entities_errors.json", "ref/ref_test02_initialize_entities_errors.json", 2),
                Arguments.of("test03", "input/test03_move_robot.json", "out/out_test03_move_robot.json", "ref/ref_test03_move_robot.json", 5),
                Arguments.of("test04", "input/test04_move_robot_errors.json", "out/out_test04_move_robot_errors.json", "ref/ref_test04_move_robot_errors.json", 2),
                Arguments.of("test05", "input/test05_env_condition.json", "out/out_test05_env_condition.json", "ref/ref_test05_env_condition.json", 2),
                Arguments.of("test06", "input/test06_update_battery.json", "out/out_test06_update_battery.json", "ref/ref_test06_update_battery.json", 3),
                Arguments.of("test07", "input/test07_update_battery_errors.json", "out/out_test07_update_battery_errors.json", "ref/ref_test07_update_battery_errors.json", 2),
                Arguments.of("test08", "input/test08_change_weather.json", "out/out_test08_change_weather.json", "ref/ref_test08_change_weather.json", 3),
                Arguments.of("test09", "input/test09_scan_plant.json", "out/out_test09_scan_plant.json", "ref/ref_test09_scan_plant.json", 3),
                Arguments.of("test10", "input/test10_scan_water.json", "out/out_test10_scan_water.json", "ref/ref_test10_scan_water.json", 5),
                Arguments.of("test11", "input/test11_scan_animal.json", "out/out_test11_scan_animal.json", "ref/ref_test11_scan_animal.json", 6),
                Arguments.of("test12", "input/test12_scan_object_errors.json", "out/out_test12_scan_object_errors.json", "ref/ref_test12_scan_object_errors.json", 2),
                Arguments.of("test13", "input/test13_learn_fact.json", "out/out_test13_learn_fact.json", "ref/ref_test13_learn_fact.json", 4),
                Arguments.of("test14", "input/test14_improve_environment.json", "out/out_test14_improve_environment.json", "ref/ref_test14_improve_environment.json", 5),
                Arguments.of("test15", "input/test15_improve_environment_errors.json", "out/out_test15_improve_environment_errors.json", "ref/ref_test15_improve_environment_errors.json", 2),
                Arguments.of("test16", "input/test16_mid.json", "out/out_test16_mid.json", "ref/ref_test16_mid.json", 6),
                Arguments.of("test17", "input/test17_multiple_simulations.json", "out/out_test17_multiple_simulations.json", "ref/ref_test17_multiple_simulations.json", 3),
                Arguments.of("test18", "input/test18_multiple_simulations_error.json", "out/out_test18_multiple_simulations_error.json", "ref/ref_test18_multiple_simulations_error.json", 2),
                Arguments.of("test19", "input/test19_complex_simple.json", "out/out_test19_complex_simple.json", "ref/ref_test19_complex_simple.json", 6),
                Arguments.of("test20", "input/test20_complex_errors.json", "out/out_test20_complex_errors.json", "ref/ref_test20_complex_errors.json", 6),
                Arguments.of("test21", "input/test21_complex_combined.json", "out/out_test21_complex_combined.json", "ref/ref_test21_complex_combined.json", 8)
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    public void run(
            final String testName,
            final String inputPath,
            final String outputPath,
            final String refPath,
            final int points
    ) throws IOException {
        Main.action(inputPath, outputPath);

        JsonNode inputJson = objectMapper.readTree(new File(inputPath));
        JsonNode outputJson = objectMapper.readTree(new File(outputPath));
        JsonNode refJson = objectMapper.readTree(new File(refPath));

        try {
            assertThatJson(outputJson).isEqualTo(refJson);
            devmindResults.add(new DevmindResult(
                testName,
                inputJson,
                outputJson,
                refJson,
                PASSED,
                points
            ));
        } catch (AssertionError e) {
            devmindResults.add(new DevmindErrorResult(
                    testName,
                    inputJson,
                    outputJson,
                    refJson,
                    points,
                    e.getMessage()
            ));
            throw e;
        }

    }

    @Test
    public void testCheckstyle() throws CheckstyleException, IOException {
        File configFile = new File("src/test/resources/checkstyle/checkstyle.xml");
        Configuration config = ConfigurationLoader.loadConfiguration(
                new InputSource(configFile.getAbsolutePath()),
                new PropertiesExpander(System.getProperties()),
                ConfigurationLoader.IgnoredModulesOptions.EXECUTE
        );

        CheckstyleAuditListener checkstyleAuditListener = new CheckstyleAuditListener();
        Checker checker = new Checker();
        checker.setModuleClassLoader(CheckerConstants.class.getClassLoader());
        checker.addListener(checkstyleAuditListener);
        checker.configure(config);

        List<File> files = SourceFileCollector.getJavaSourceFiles("src/main/java");
        int errorCount = checker.process(files);
        checker.destroy();

        String checkStyleErrors = checkstyleAuditListener.toString();
        try {
            assertThat(errorCount).isLessThanOrEqualTo(CheckerConstants.MAXIMUM_ERROR_CHECKSTYLE);
            devmindResults.add(new DevmindResult(
                    "checkstyle",
                    "",
                    "",
                    errorCount,
                    PASSED,
                    CheckerConstants.CHECKSTYLE_POINTS
            ));
            System.out.println(checkStyleErrors);
        }
        catch (AssertionError e) {
            devmindResults.add(new DevmindErrorResult(
                    "checkstyle",
                    "",
                    "",
                    errorCount,
                    10,
                    e.getMessage()
            ));
            throw new CheckstyleException(checkStyleErrors);
        }
    }

    private boolean hasNonDeveloperAuthor(RevCommit commit) {
        List<String> exceptedAuthors = List.of(
            "david.capragiu@gmail.com",
            "alina.tudorache872@gmail.com",
            "63539529+Dievaid@users.noreply.github.com",
            "69516563+alina-t-872@users.noreply.github.com"
        );

        String userEmail = commit.getAuthorIdent().getEmailAddress();
        return !exceptedAuthors.contains(userEmail);
    }

    @Test
    public void testGitCommits() throws GitAPIException, IOException {
        File repoDirectory = new File("./");

        try (Git git = Git.open(repoDirectory)) {
            List<RevCommit> commits = StreamSupport.stream(git.log().call().spliterator(), false)
                    .filter(this::hasNonDeveloperAuthor)
                    .sorted(Comparator.comparing(RevCommit::getCommitTime))
                    .toList();

            assertThat(commits).isNotNull();
            assertThat(commits.size())
                    .isGreaterThanOrEqualTo(CheckerConstants.GIT_MINIMUM_COMMITS);

            devmindResults.add(new DevmindResult(
                    "git",
                    "",
                    "",
                    "",
                    PASSED,
                    CheckerConstants.GIT_POINTS
            ));
        } catch (IOException | AssertionError | GitAPIException e) {
            devmindResults.add(new DevmindErrorResult(
                    "git",
                    "",
                    "",
                    "",
                    CheckerConstants.GIT_POINTS,
                    e.getMessage()
            ));
            throw e;
        }
    }

    @AfterAll
    public static void afterAll() throws JsonProcessingException {
        boolean isDevmindEnvironment = Optional.ofNullable(System.getProperty("environment"))
                .map(value -> value.equals("devmind"))
                .orElse(false);

        if (isDevmindEnvironment) {
            printDevmindResults();
        } else {
            printLocalResults();
        }
    }

    private static void printLocalResults() {
        System.out.println(TestCaseWatcher.stringBuilder);
        System.out.println("Total: " + TestCaseWatcher.totalPoints + "/100");

        boolean allPassed = devmindResults.stream()
                .allMatch(result -> PASSED.equals(result.getStatus()));

        if (allPassed) {
            System.out.println("Yey, ai reusit sa-l ajuti cu succes pe TerraBot sa descopere planeta \uD83D\uDE0A\"");
            System.out.println("Uite un mic gest de recunostinta ❤");
            System.out.println("https://youtu.be/coXOFBjLjHI?si=IT1bHEOEs7kHea1m");
        }
    }

    private static void printDevmindResults() throws JsonProcessingException {
        System.out.println("BEGIN-DEVMIND-TEST-RESULTS");
        System.out.println(objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(devmindResults)
        );
        System.out.println("END-DEVMIND-TEST-RESULTS");
    }
}
