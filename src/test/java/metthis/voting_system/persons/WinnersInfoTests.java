package metthis.voting_system.persons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class WinnersInfoTests {
    @ParameterizedTest
    @MethodSource("validWinnersAndTiesProvider")
    void canGetWinners(Candidate[] winners, int[] ties) {
        WinnersInfo winnersInfo = new WinnersInfo(winners, ties);
        Candidate[] actual = winnersInfo.getWinners();
        assertEquals(winners, actual);
    }

    @ParameterizedTest
    @MethodSource("validWinnersAndTiesProvider")
    void canGetTies(Candidate[] winners, int[] ties) {
        WinnersInfo winnersInfo = new WinnersInfo(winners, ties);
        int[] actual = winnersInfo.getTies();
        assertEquals(ties, actual);
    }

    static Stream<Arguments> validWinnersAndTiesProvider() {
        String[][] winnerStringsArrays = {
                { "C1", "C2", "C3", "C4", "C5", "C6" }
        };
        int[][] tiesArrays = {
                { 0, 0, -1, 3, 3, 3 }
        };

        Candidate[][] winnersArrays = getWinnersArrays(winnerStringsArrays);
        return twoTwoDimensionalArraysToStreamOfArguments(winnersArrays, tiesArrays);
    }

    static Candidate[][] getWinnersArrays(String[][] winnerStringsArrays) {
        int howManySetsOfArguments = winnerStringsArrays.length;
        Candidate[][] winnersArrays = new Candidate[howManySetsOfArguments][];

        for (int i = 0; i < howManySetsOfArguments; i++) {
            String[] winnerStrings = winnerStringsArrays[i];
            winnersArrays[i] = new Candidate[winnerStrings.length];
            for (int j = 0; j < winnerStrings.length; j++) {
                String string = winnerStrings[j];
                Candidate winner;
                if (string == null) {
                    winner = null;
                } else {
                    winner = new Candidate(string, string, "1960-01-01", true, "2020-01-01");
                }
                winnersArrays[i][j] = winner;
            }
        }

        return winnersArrays;
    }

    static Stream<Arguments> twoTwoDimensionalArraysToStreamOfArguments(
            Candidate[][] winnersArrays, int[][] tiesArrays) {
        if (winnersArrays.length != tiesArrays.length) {
            throw new IllegalArgumentException("Both 2-dimensional arrays must be the same length");
        }

        List<Arguments> argumentsList = new ArrayList<>();
        for (int i = 0; i < winnersArrays.length; i++) {
            Arguments arguments = Arguments.of(winnersArrays[i], tiesArrays[i]);
            argumentsList.add(arguments);
        }

        return argumentsList.stream();
    }

    @ParameterizedTest
    @MethodSource
    void unequalLenghtsCauseIllegalArgumentException(Candidate[] winners, int[] ties) {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new WinnersInfo(winners, ties);
        });

        String expectedMessage = "Both arrays must be of same lenght but are " +
                winners.length + " and " + ties.length;

        assertEquals(expectedMessage, thrown.getMessage());
    }

    static Stream<Arguments> unequalLenghtsCauseIllegalArgumentException() {
        String[][] winnerStringsArrays = {
                { "C1", "C2" },
                { "C1", "C2", "C3" },
                { "C1" },
                {}
        };
        int[][] tiesArrays = {
                { -1, -1, -1 },
                { -1, -1 },
                {},
                { -1 }
        };

        Candidate[][] winnersArrays = getWinnersArrays(winnerStringsArrays);
        return twoTwoDimensionalArraysToStreamOfArguments(winnersArrays, tiesArrays);
    }

    @ParameterizedTest
    @MethodSource
    void nullCandidateCausesIllegalArgumentException(Candidate[] winners, int[] ties) {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new WinnersInfo(winners, ties);
        });

        String expectedMessage = "At least one winner is null";

        assertEquals(expectedMessage, thrown.getMessage());
    }

    static Stream<Arguments> nullCandidateCausesIllegalArgumentException() {
        String[][] winnerStringsArrays = {
                { null },
                { "C1", null },
                { null, "C2" },
                { "C1", null, null }
        };
        int[][] tiesArrays = {
                { -1 },
                { -1, -1 },
                { -1, -1 },
                { -1, -1, -1 }
        };

        Candidate[][] winnersArrays = getWinnersArrays(winnerStringsArrays);
        return twoTwoDimensionalArraysToStreamOfArguments(winnersArrays, tiesArrays);
    }

    @ParameterizedTest
    @MethodSource
    void tieValueWhichIsTooHighCausesIllegalArgumentException(
            Candidate[] winners, int[] ties) {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new WinnersInfo(winners, ties);
        });

        String expectedMessage = "At least one tie value is larger than or equal to the number of winners";

        assertEquals(expectedMessage, thrown.getMessage());
    }

    static Stream<Arguments> tieValueWhichIsTooHighCausesIllegalArgumentException() {
        String[][] winnerStringsArrays = {
                { "C1" },
                { "C1", "C2" },
                { "C1", "C2", "C3" },
                { "C1", "C2", "C3", "C4" }
        };
        int[][] tiesArrays = {
                { 1 },
                { 0, 2 },
                { 3, -1, 3 },
                { 0, 0, -1, 5 }
        };

        Candidate[][] winnersArrays = getWinnersArrays(winnerStringsArrays);
        return twoTwoDimensionalArraysToStreamOfArguments(winnersArrays, tiesArrays);
    }
}
