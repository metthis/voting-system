package metthis.voting_system.logic;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.voting.ParametrisedUtil;

public class GetNumberOfWinnersArgumentsProvider implements ArgumentsProvider {
    private static Candidate[] candidates() {
        return ParametrisedUtil.getDifferenCandidates(10);
    }

    private static List<Integer> expectedList() {
        return Arrays.asList(
                0, 0, 0, 1, 2, 2, 3, 3, 2, 2, 10);
    }

    private static List<List<List<Integer>>> winnersBlueprints() {
        return Arrays.asList(
                Arrays.asList(),
                Arrays.asList(
                        Arrays.asList()),
                Arrays.asList(
                        Arrays.asList(),
                        Arrays.asList()),
                Arrays.asList(
                        Arrays.asList(0)),
                Arrays.asList(
                        Arrays.asList(0, 1)),
                Arrays.asList(
                        Arrays.asList(0),
                        Arrays.asList(1)),
                Arrays.asList(
                        Arrays.asList(0, 1),
                        Arrays.asList(2)),
                Arrays.asList(
                        Arrays.asList(0),
                        Arrays.asList(1, 2)),
                Arrays.asList(
                        Arrays.asList(0, 1),
                        Arrays.asList()),
                Arrays.asList(
                        Arrays.asList(),
                        Arrays.asList(0, 1)),
                Arrays.asList(
                        Arrays.asList(0, 1, 2),
                        Arrays.asList(3),
                        Arrays.asList(4, 5),
                        Arrays.asList(6),
                        Arrays.asList(),
                        Arrays.asList(7),
                        Arrays.asList(8, 9)));
    }

    private static List<Candidate[][]> winnersList() {
        return ParametrisedUtil.getWinnersListFromBlueprints(
                winnersBlueprints(), candidates());
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return ParametrisedUtil.multipleListsToStreamOfArguments(
                expectedList(), winnersList());
    }
}
