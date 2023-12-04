import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day04 {

    int partOne(List<String> input) {
        return input.stream()
                .map(Card::of)
                .mapToInt(Card::score)
                .sum();
    }

    int partTwo(List<String> input) {
        Map<Integer, Integer> initialCounts = IntStream.range(0, input.size())
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> 1));

        return collectCards(input, initialCounts, 0).values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    Map<Integer, Integer> collectCards(List<String> input, Map<Integer, Integer> counts, int current) {
        if (current >= input.size()) {
            return counts;
        }

        int matches = Card.of(input.get(current)).matches();
        Map<Integer, Integer> updatedCounts = new HashMap<>(counts);
        IntStream.rangeClosed(1, matches)
                .forEach(match -> updatedCounts.merge(
                        current + match, updatedCounts.getOrDefault(current, 1),
                        Integer::sum
                ));

        return collectCards(input, updatedCounts, current + 1);
    }

    record Card(Set<Integer> winningNumbers, Set<Integer> playerNumbers) {
        static Card of(String input) {
            String[] parts = input.substring(input.indexOf(':') + 1).split(" \\| ");
            return new Card(parseNumbers(parts[0]), parseNumbers(parts[1]));
        }

        static Set<Integer> parseNumbers(String input) {
            return Arrays.stream(input.trim().split("\\s+"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
        }

        int score() {
            int matchCount = matches();
            return matchCount > 0 ? 1 << (matchCount - 1) : 0;
        }

        int matches() {
            return playerNumbers.stream()
                    .reduce(0, (count, number) ->
                            winningNumbers.contains(number) ? count + 1 : count, Integer::sum
                    );
        }
    }
}
