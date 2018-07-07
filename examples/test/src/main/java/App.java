import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class App {
    public static void main(String[] args) {
        new App().cat(args[0]);
    }

    public void cat(String name) {
        try {
            final List<String> content = Files.readAllLines(Paths.get(name));
            System.out.println("> cat " + name);
            for (String line : content) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}