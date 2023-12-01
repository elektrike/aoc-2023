import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day01 {

    Map<String, String> wordToNumeric = Map.of(
            "one", "1", "two", "2", "three", "3", "four", "4", "five", "5",
            "six", "6", "seven", "7", "eight", "8", "nine", "9"
    );

    String toNumeric(String str) {
        return wordToNumeric.getOrDefault(str, str);
    }

    int partOne(List<String> input) {
        return input.stream()
                .map(line -> line.chars()
                        .mapToObj(c -> (char) c)
                        .filter(Character::isDigit)
                        .collect(Collectors.collectingAndThen(
                                Collectors.toList(),
                                cl -> new DigitPair(String.valueOf(cl.getFirst()), String.valueOf(cl.getLast()))
                        )))
                .mapToInt(dp -> Integer.parseInt(dp.first + dp.last))
                .sum();
    }

    int partTwo(List<String> input) {
        return input.stream()
                .map(line -> Pattern
                        .compile("(?i)(?=(" + String.join("|", wordToNumeric.keySet()) + "|\\d))")
                        .matcher(line)
                        .results()
                        .map(matchResult -> matchResult.group(1))
                        .collect(Collectors.collectingAndThen(
                                Collectors.toList(),
                                cl -> new DigitPair(cl.getFirst(), cl.getLast())
                        )))
                .mapToInt(dp -> Integer.parseInt(toNumeric(dp.first) + toNumeric(dp.last)))
                .sum();
    }

    record DigitPair(String first, String last) {}
}
