package metthis.voting_system.voting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import metthis.voting_system.persons.Candidate;

// This class is also used by tests in the logic package.
// The class should thus be split depending on its methods' usage
// and put into appropriate packages.

public class ParametrisedUtil {
    public static Candidate[] getDifferenCandidates(int count) {
        Candidate[] candidates = new Candidate[count];
        for (int i = 0; i < count; i++) {
            String iString = String.valueOf(i);
            candidates[i] = new Candidate(iString, iString, "1960-01-01", true, "2020-01-01");
        }
        return candidates;
    }

    public static List<List<BallotBox>> getBallotBoxGroupsFromBlueprints(
            List<List<Integer>> blueprints,
            Candidate[] candidates) {
        List<List<BallotBox>> ballotBoxGroups = new ArrayList<>();
        for (List<Integer> blueprint : blueprints) {
            List<BallotBox> ballotBoxGroup = new ArrayList<>();
            for (int number : blueprint) {
                if (number < 0) {
                    ballotBoxGroup.add(new BallotBox());
                } else {
                    int groupSize = ballotBoxGroup.size();
                    BallotBox lastAddedBallotBox = ballotBoxGroup.get(groupSize - 1);
                    Vote vote = new SingleCandidateSingleChoiceVote(candidates[number]);
                    lastAddedBallotBox.add(vote);
                }
            }
            ballotBoxGroups.add(ballotBoxGroup);
        }
        return ballotBoxGroups;
    }

    public static List<Map<Vote, Integer>> getVotesListFromBlueprints(
            List<Map<Integer, Integer>> blueprints,
            Candidate[] candidates) {
        List<Map<Vote, Integer>> expectedVotesList = new ArrayList<>();
        for (Map<Integer, Integer> blueprint : blueprints) {
            Map<Vote, Integer> expectedVotes = new HashMap<>();
            for (Map.Entry<Integer, Integer> entry : blueprint.entrySet()) {
                Vote vote = new SingleCandidateSingleChoiceVote(
                        candidates[entry.getKey()]);
                expectedVotes.put(vote, entry.getValue());
            }
            expectedVotesList.add(expectedVotes);
        }
        return expectedVotesList;
    }

    public static List<BallotBox> getBallotBoxListFromVotesList(
            List<Map<Vote, Integer>> votesList) {
        List<BallotBox> ballotBoxList = new ArrayList<>();
        for (Map<Vote, Integer> votes : votesList) {
            BallotBox ballotBox = new BallotBox();
            for (Map.Entry<Vote, Integer> entry : votes.entrySet()) {
                ballotBox.add(entry.getKey(), entry.getValue());
            }
            ballotBoxList.add(ballotBox);
        }
        return ballotBoxList;
    }

    public static List<BallotBox> getBallotBoxesFromBlueprints(
            List<Map<Integer, Integer>> blueprints,
            Candidate[] candidates) {
        List<Map<Vote, Integer>> votesList = getVotesListFromBlueprints(
                blueprints, candidates);
        return getBallotBoxListFromVotesList(votesList);
    }

    public static List<VotingRound> getVotingRoundsFromBallotBoxes(
            List<BallotBox> ballotBoxes) {
        List<VotingRound> rounds = new ArrayList<>();
        for (BallotBox ballotBox : ballotBoxes) {
            VotingRound round = new VotingRound();
            round.setBallotBoxes(Arrays.asList(ballotBox));
            rounds.add(round);
        }
        return rounds;
    }

    public static List<VotingRound> getVotingRoundsFromBlueprints(
            List<Map<Integer, Integer>> blueprints,
            Candidate[] candidates) {
        List<BallotBox> ballotBoxes = getBallotBoxesFromBlueprints(blueprints, candidates);
        return getVotingRoundsFromBallotBoxes(ballotBoxes);
    }

    public static List<Candidate[][]> getWinnersListFromBlueprints(
            List<List<List<Integer>>> blueprints,
            Candidate[] candidates) {
        List<Candidate[][]> winnersList = new ArrayList<>();
        for (List<List<Integer>> blueprint : blueprints) {
            List<Candidate[]> winners = new ArrayList<>();
            for (List<Integer> group : blueprint) {
                Candidate[] tiedWinners = new Candidate[group.size()];
                for (int i = 0; i < group.size(); i++) {
                    Candidate winner = candidates[group.get(i)];
                    tiedWinners[i] = winner;
                }
                winners.add(tiedWinners);
            }
            winnersList.add(winners.toArray(Candidate[][]::new));
        }
        return winnersList;
    }

    public static Stream<Arguments> multipleListsToStreamOfArguments(List<?>... lists) {
        int listSize = lists[0].size();
        for (List<?> list : lists) {
            if (list.size() != listSize) {
                throw new IllegalArgumentException("All lists must be the same length");
            }
        }

        List<Arguments> argumentsList = new ArrayList<>();

        for (int i = 0; i < listSize; i++) {
            Object[] values = new Object[lists.length];
            for (int j = 0; j < lists.length; j++) {
                values[j] = lists[j].get(i);
            }
            Arguments arguments = Arguments.of(values);
            argumentsList.add(arguments);
        }

        return argumentsList.stream();
    }
}
