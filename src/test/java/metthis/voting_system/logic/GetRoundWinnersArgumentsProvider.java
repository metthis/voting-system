package metthis.voting_system.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.voting.ParametrisedUtil;
import metthis.voting_system.voting.VotingRound;

public class GetRoundWinnersArgumentsProvider implements ArgumentsProvider {
    /*
     * Test cases:
     * 
     * 1) 0 candidates, 0 usual winners, 0 winners
     * 2) 0 candidates, 1 usual winner, 0 winners
     * 3) 0 candidates, 2 usual winners, 0 winners
     * 4) 1 candidate, 0 usual winners, 0 winners
     * 5) 1 candidate, 1 usual winner, 1 winner
     * 6) 1 candidate, 2 usual winners, 1 winner
     * 7) 2 candidates no ties, 0 usual winners, 0 winners
     * 8) 2 candidates no ties, 1 usual winner, 1 winner
     * 9) 2 candidates no ties, 2 usual winners, 2 winners
     * 10) 2 candidates no ties, 3 usual winners, 2 winners
     * 11) 2 tied candidates, 0 usual winners, 0 winners
     * 12) 2 tied candidates, 1 usual winner, 2 winners
     * 13) 2 tied candidates, 2 usual winners, 2 winners
     * 14) 2 tied candidates, 3 usual winners, 2 winners
     * 15) 2 tied candidates + 1, 1 usual winner, 2 winners
     * 16) 2 tied candidates + 1, 2 usual winners, 2 winners
     * 17) 2 tied candidates + 1, 3 usual winners, 3 winners
     * 18) 2 tied candidates + 1, 4 usual winners, 3 winners
     * 19) 1 candidate + 2 tied candidates, 1 usual winner, 1 winner
     * 20) 1 candidate + 2 tied candidates, 2 usual winners, 3 winners
     * 21) 1 candidate + 2 tied candidates, 3 usual winners, 3 winners
     * 22) 1 candidate + 2 tied candidates, 4 usual winners, 3 winners
     * 23) 3 tied candidates, 1 usual winner, 3 winners
     * 24) 3 tied candidates, 2 usual winners, 3 winners
     * 25) 3 tied candidates, 3 usual winners, 3 winners
     * 26) 3 tied candidates, 4 usual winners, 3 winners
     * 27) 3 tied candidates + 1, 1 usual winner, 3 winners
     * 28) 3 tied candidates + 1, 2 usual winners, 3 winners
     * 29) 3 tied candidates + 1, 3 usual winners, 3 winners
     * 30) 3 tied candidates + 1, 4 usual winners, 4 winners
     * 31) 3 tied candidates + 1, 5 usual winners, 4 winners
     * 32) 2 tied + 2 tied, 1 usual winner, 2 winners
     * 33) 2 tied + 2 tied, 2 usual winners, 2 winners
     * 34) 2 tied + 2 tied, 3 usual winners, 4 winners
     * 35) 2 tied + 2 tied, 4 usual winners, 4 winners
     * 36) 2 tied + 2 tied, 5 usual winners, 4 winners
     * 37) 2 tied + 1 + 2 tied, 1 usual winner, 2 winners
     * 38) 2 tied + 1 + 2 tied, 2 usual winners, 2 winners
     * 39) 2 tied + 1 + 2 tied, 3 usual winners, 3 winners
     * 40) 2 tied + 1 + 2 tied, 4 usual winners, 5 winners
     * 41) 2 tied + 1 + 2 tied, 5 usual winners, 5 winners
     * 42) 2 tied + 1 + 2 tied, 6 usual winners, 5 winners
     * 
     * With an ineligible candidate:
     * 
     * 43) 1 ineligible candidate (most votes), 1 candidate + 2 tied candidates, 1
     * usual winner, 1 winner
     * 44) 1 ineligible candidate (most votes), 1 candidate + 2 tied candidates, 2
     * usual winners, 3 winners
     * 45) 1 ineligible candidate (most votes), 1 candidate + 2 tied candidates, 3
     * usual winners, 3 winners
     * 46) 1 ineligible candidate (most votes), 1 candidate + 2 tied candidates, 4
     * usual winners, 3 winners
     * 
     * 6 different permuations of entering the same data into a map:
     * 47-52) 1 + 1 + 1 candidate no ties, 2 usual winners, 2 winners
     */

    private static Candidate[] candidates() {
        return ParametrisedUtil.getDifferenCandidates(6);
    }

    private static List<Map<Integer, Integer>> roundBlueprints() {
        List<Map<Integer, Integer>> roundBlueprints = new ArrayList<>();
        // 1-3: 0 candidates
        roundBlueprints.addAll(Collections.nCopies(3, Map.of()));
        // 4-6: 1 candidate
        roundBlueprints.addAll(Collections.nCopies(3, Map.of(
                0, 10)));
        // 7-10: 2 candidates no ties
        roundBlueprints.addAll(Collections.nCopies(4, Map.of(
                0, 20,
                1, 10)));
        // 11-14: 2 tied candidates
        roundBlueprints.addAll(Collections.nCopies(4, Map.of(
                0, 20,
                1, 20)));
        // 15-18: 2 tied candidates + 1
        roundBlueprints.addAll(Collections.nCopies(4, Map.of(
                0, 20,
                1, 20,
                2, 10)));
        // 19-22: 1 candidate + 2 tied candidates
        roundBlueprints.addAll(Collections.nCopies(4, Map.of(
                0, 20,
                1, 10,
                2, 10)));
        // 23-26: 3 tied candidates
        roundBlueprints.addAll(Collections.nCopies(4, Map.of(
                0, 20,
                1, 20,
                2, 20)));
        // 27-31: 3 tied candidates + 1
        roundBlueprints.addAll(Collections.nCopies(5, Map.of(
                0, 20,
                1, 20,
                2, 20,
                3, 10)));
        // 32-36: 2 tied + 2 tied
        roundBlueprints.addAll(Collections.nCopies(5, Map.of(
                0, 20,
                1, 20,
                2, 10,
                3, 10)));
        // 37-42: 2 tied + 1 + 2 tied
        roundBlueprints.addAll(Collections.nCopies(6, Map.of(
                0, 30,
                1, 30,
                2, 20,
                3, 10,
                4, 10)));
        // 43-46: 1 ineligible candidate (most votes), 1 candidate + 2 tied candidates
        roundBlueprints.addAll(Collections.nCopies(4, Map.of(
                5, 100,
                0, 20,
                1, 10,
                2, 10)));
        // 47: 1 + 1 + 1 candidate no ties, 2 usual winners, 2 winners (permutation 1)
        roundBlueprints.addAll(Collections.nCopies(1, Map.of(
                1, 10,
                2, 20,
                3, 30)));
        // 48: 1 + 1 + 1 candidate no ties, 2 usual winners, 2 winners (permutation 1)
        roundBlueprints.addAll(Collections.nCopies(1, Map.of(
                1, 10,
                3, 30,
                2, 20)));
        // 49: 1 + 1 + 1 candidate no ties, 2 usual winners, 2 winners (permutation 1)
        roundBlueprints.addAll(Collections.nCopies(1, Map.of(
                2, 20,
                1, 10,
                3, 30)));
        // 50: 1 + 1 + 1 candidate no ties, 2 usual winners, 2 winners (permutation 1)
        roundBlueprints.addAll(Collections.nCopies(1, Map.of(
                2, 20,
                3, 30,
                1, 10)));
        // 51: 1 + 1 + 1 candidate no ties, 2 usual winners, 2 winners (permutation 1)
        roundBlueprints.addAll(Collections.nCopies(1, Map.of(
                3, 30,
                1, 10,
                2, 20)));
        // 52: 1 + 1 + 1 candidate no ties, 2 usual winners, 2 winners (permutation 1)
        roundBlueprints.addAll(Collections.nCopies(1, Map.of(
                3, 30,
                2, 20,
                1, 10)));

        return roundBlueprints;
    }

    private static List<Integer> usualNumbersOfWinners() {
        return Arrays.asList(
                // 1-3:
                0, 1, 2,
                // 4-6:
                0, 1, 2,
                // 7-10:
                0, 1, 2, 3,
                // 11-14:
                0, 1, 2, 3,
                // 15-18:
                1, 2, 3, 4,
                // 19-22:
                1, 2, 3, 4,
                // 23-26:
                1, 2, 3, 4,
                // 27-31:
                1, 2, 3, 4, 5,
                // 32-36:
                1, 2, 3, 4, 5,
                // 37-42:
                1, 2, 3, 4, 5, 6,
                // 43-46:
                1, 2, 3, 4,
                // 47-52:
                2, 2, 2, 2, 2, 2);
    }

    private static List<List<List<Integer>>> winnersBlueprints() {
        List<List<List<Integer>>> winnersBlueprints = new ArrayList<>();
        // 1-4: 0 winners
        winnersBlueprints.addAll(Collections.nCopies(4, Arrays.asList()));
        // 5-6: 1 winner
        winnersBlueprints.addAll(Collections.nCopies(2, Arrays.asList(
                Arrays.asList(0))));
        // 7: 0 winners
        winnersBlueprints.addAll(Collections.nCopies(1, Arrays.asList()));
        // 8: 1 winner
        winnersBlueprints.addAll(Collections.nCopies(1, Arrays.asList(
                Arrays.asList(0))));
        // 9-10: 2 winners no ties
        winnersBlueprints.addAll(Collections.nCopies(2, Arrays.asList(
                Arrays.asList(0),
                Arrays.asList(1))));
        // 11: 0 winners
        winnersBlueprints.addAll(Collections.nCopies(1, Arrays.asList()));
        // 12-16: 2 winners tied
        winnersBlueprints.addAll(Collections.nCopies(5, Arrays.asList(
                Arrays.asList(0, 1))));
        // 17-18: 3 winners, 1-2 tied
        winnersBlueprints.addAll(Collections.nCopies(2, Arrays.asList(
                Arrays.asList(0, 1),
                Arrays.asList(2))));
        // 19: 1 winner
        winnersBlueprints.addAll(Collections.nCopies(1, Arrays.asList(
                Arrays.asList(0))));
        // 20-22: 3 winners, 2-3 tied
        winnersBlueprints.addAll(Collections.nCopies(3, Arrays.asList(
                Arrays.asList(0),
                Arrays.asList(1, 2))));
        // 23-29: 3 winners tied
        winnersBlueprints.addAll(Collections.nCopies(7, Arrays.asList(
                Arrays.asList(0, 1, 2))));
        // 30-31: 4 winners, 1-3 tied
        winnersBlueprints.addAll(Collections.nCopies(2, Arrays.asList(
                Arrays.asList(0, 1, 2),
                Arrays.asList(3))));
        // 32-33: 2 winners tied
        winnersBlueprints.addAll(Collections.nCopies(2, Arrays.asList(
                Arrays.asList(0, 1))));
        // 34-36: 4 winners, 1-2 tied, 3-4 tied
        winnersBlueprints.addAll(Collections.nCopies(3, Arrays.asList(
                Arrays.asList(0, 1),
                Arrays.asList(2, 3))));
        // 37-38: 2 winners tied
        winnersBlueprints.addAll(Collections.nCopies(2, Arrays.asList(
                Arrays.asList(0, 1))));
        // 39: 3 winners, 1-2 tied
        winnersBlueprints.addAll(Collections.nCopies(1, Arrays.asList(
                Arrays.asList(0, 1),
                Arrays.asList(2))));
        // 40-42: 5 winners, 1-2 tied, 3-5 tied
        winnersBlueprints.addAll(Collections.nCopies(3, Arrays.asList(
                Arrays.asList(0, 1),
                Arrays.asList(2),
                Arrays.asList(3, 4))));
        // 43: 1 winner
        winnersBlueprints.addAll(Collections.nCopies(1, Arrays.asList(
                Arrays.asList(0))));
        // 44-46: 3 winners, 2-3 ties
        winnersBlueprints.addAll(Collections.nCopies(3, Arrays.asList(
                Arrays.asList(0),
                Arrays.asList(1, 2))));
        // 47-52: 2 winners no ties (candidates 3 and 2):
        winnersBlueprints.addAll(Collections.nCopies(6, Arrays.asList(
                Arrays.asList(3),
                Arrays.asList(2))));

        return winnersBlueprints;
    }

    private static List<VotingRound> rounds() {
        return ParametrisedUtil
                .getVotingRoundsFromBlueprints(
                        roundBlueprints(), candidates());
    }

    private static List<Candidate[][]> expectedWinnersList() {
        return ParametrisedUtil
                .getWinnersListFromBlueprints(
                        winnersBlueprints(), candidates());
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return ParametrisedUtil.multipleListsToStreamOfArguments(
                rounds(),
                usualNumbersOfWinners(),
                expectedWinnersList());
    }
}
