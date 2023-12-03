import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day03 {

    int partOne(List<String> input) {
        char[][] grid = parseGrid(input);
        List<Number> numbers = getNumbers(grid);
        Set<Position> symbolPositions = getSymbolPositions(grid);

        return numbers.stream()
                .filter(number -> isAdjacent(number, symbolPositions))
                .mapToInt(Number::value)
                .sum();
    }

    int partTwo(List<String> input) {
        char[][] grid = parseGrid(input);
        List<Number> numbers = getNumbers(grid);
        Set<Position> gearPositions = getGearPositions(grid);

        return gearPositions.stream()
                .map(gear -> numbers.stream()
                        .filter(number -> isAdjacent(number, gear))
                        .collect(Collectors.toSet()))
                .filter(adjacentNumbers -> adjacentNumbers.size() == 2)
                .mapToInt(adjacentNumbers -> adjacentNumbers.stream()
                        .mapToInt(Number::value)
                        .reduce(1, Math::multiplyExact))
                .sum();
    }

    char[][] parseGrid(List<String> input) {
        return input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    List<Number> getNumbers(char[][] grid) {
        return IntStream.range(0, grid.length)
                .boxed()
                .flatMap(y -> IntStream.range(0, grid[y].length)
                        .filter(x -> Character.isDigit(grid[y][x])
                                && (x == 0 || !Character.isDigit(grid[y][x - 1])))
                        .mapToObj(x -> extractNumber(x, y, grid)))
                .collect(Collectors.toList());
    }

    Number extractNumber(int startX, int y, char[][] grid) {
        String numberValue = IntStream.iterate(startX, x -> x < grid[y].length
                        && Character.isDigit(grid[y][x]), x -> x + 1)
                .mapToObj(x -> String.valueOf(grid[y][x]))
                .collect(Collectors.joining());

        return new Number(Integer.parseInt(numberValue), startX, startX + numberValue.length() - 1, y);
    }

    Set<Position> getSymbolPositions(char[][] grid) {
        return getPositions(grid, (x, y) -> grid[y][x] != '.'
                && !Character.isDigit(grid[y][x]));
    }

    Set<Position> getGearPositions(char[][] grid) {
        return getPositions(grid, (x, y) -> grid[y][x] == '*');
    }

    Set<Position> getPositions(char[][] grid, BiPredicate<Integer, Integer> shouldInclude) {
        return IntStream.range(0, grid.length)
                .boxed()
                .flatMap(y -> IntStream.range(0, grid[y].length)
                        .filter(x -> shouldInclude.test(x, y))
                        .mapToObj(x -> new Position(x, y)))
                .collect(Collectors.toSet());
    }

    boolean isAdjacent(Number number, Set<Position> positions) {
        return positions.stream()
                .anyMatch(position -> isAdjacent(number, position));
    }

    boolean isAdjacent(Number number, Position position) {
        return IntStream.rangeClosed(number.startX, number.endX)
                .anyMatch(x -> Math.abs(position.x - x) <= 1
                        && Math.abs(position.y - number.y) <= 1);
    }

    record Position(int x, int y) {}

    record Number(int value, int startX, int endX, int y) {}
}
