import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class App {
    public static void main(String... args) throws IOException {
        if (args.length == 1) {
            final Path path = Paths.get(args[0]);
            if (Files.exists(path)) {
                final List<String> content = Files.readAllLines(path);
                System.out.println("> cat " + path.getFileName());
                for (final String line : content) {
                    System.out.println(line);
                }
            }
        }
    }
}