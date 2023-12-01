package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileUtil {

    public static List<String> readFileLines(String path) throws IOException {
        return Files.readAllLines(Paths.get(path));
    }

    public static List<String> readBatches(String path) throws IOException {
        return Arrays.asList(readFileAsString(path).split("\n\n"));
    }

    public static String readFileAsString(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
