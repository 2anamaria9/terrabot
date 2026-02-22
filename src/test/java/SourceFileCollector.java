import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SourceFileCollector {
    public static List<File> getJavaSourceFiles(String rootDir) throws IOException {
        try (var stream = Files.walk(Paths.get(rootDir))) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(Path::toFile)
                    .toList();
        }
    }
}
