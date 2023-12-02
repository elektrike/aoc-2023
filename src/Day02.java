import java.util.*;
import java.util.stream.Collectors;

public class Day02 {

    int partOne(List<String> input) {
        return parseGames(input).stream()
                .filter(game -> game.sets.stream()
                        .allMatch(cubes -> cubes.red <= 12 && cubes.green <= 13 && cubes.blue <= 14))
                .mapToInt(Game::id)
                .sum();
    }

    int partTwo(List<String> input) {
        return parseGames(input).stream()
                .mapToInt(game -> game.sets.stream()
                        .reduce((s1, s2) -> new Set(
                                Math.max(s1.red, s2.red),
                                Math.max(s1.green, s2.green),
                                Math.max(s1.blue, s2.blue)
                        ))
                        .map(set -> set.red * set.green * set.blue)
                        .orElse(0)
                )
                .sum();
    }

    List<Game> parseGames(List<String> input) {
        return input.stream()
                .map(line -> {
                    String[] parts = line.split(": ");
                    int id = Integer.parseInt(parts[0].split(" ")[1]);
                    String[] setsInput = parts[1].split("; ");

                    return new Game(id, Arrays.stream(setsInput)
                            .map(this::parseSet)
                            .collect(Collectors.toList()));
                })
                .collect(Collectors.toList());
    }

    Set parseSet(String setInput) {
        return Arrays.stream(setInput.split(", "))
                .map(cubeInput -> cubeInput.split(" "))
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                parts -> Color.valueOf(parts[1].toUpperCase()),
                                parts -> Integer.parseInt(parts[0])
                        ),
                        cubes -> new Set(cubes.getOrDefault(Color.RED, 0),
                                         cubes.getOrDefault(Color.GREEN, 0),
                                         cubes.getOrDefault(Color.BLUE, 0))
                ));
    }

    record Game(int id, List<Set> sets) {}

    record Set(int red, int green, int blue) {}

    enum Color {
        RED, GREEN, BLUE
    }
}
