package metthis.voting_system.voting;

import metthis.voting_system.persons.Candidate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VotingRoundTests {
    @Nested
    class New {
        @Test
        void isInstantiatedWithNew() {
            new VotingRound();
        }
    }

    @Nested
    class WhenNew {
        private VotingRound votingRound;

        @BeforeEach
        void initVotingRound() {
            this.votingRound = new VotingRound();
        }

        @Test
        void canCreateBallotBox() {
            votingRound.createBallotBox();
        }

        @Test
        void createBallotBoxReturnsEmptyBallotBox() {
            BallotBox ballotBox = votingRound.createBallotBox();
            assertEquals(0, ballotBox.getVotes().size());
        }

        @Test
        void canGetBallotBoxes() {
            votingRound.getBallotBoxes();
        }

        @Test
        void creatingBallotBoxOnceAndGettingBallotBoxesReturnsCollectionWithOneBallotBox() {
            votingRound.createBallotBox();
            Collection<BallotBox> ballotBoxes = votingRound.getBallotBoxes();
            assertEquals(1, ballotBoxes.size());
        }

        @Test
        void creatingBallotBoxTwiceAndGettingBallotBoxesReturnsCollectionWithTwoBallotBoxes() {
            votingRound.createBallotBox();
            votingRound.createBallotBox();
            Collection<BallotBox> ballotBoxes = votingRound.getBallotBoxes();
            assertEquals(2, ballotBoxes.size());
        }

        @Test
        void canGetVotes() {
            votingRound.getVotes();
        }

        @Test
        void getVotesInitiallyReturnsEmptyMap() {
            assertTrue(votingRound.getVotes().isEmpty());
        }

        @ParameterizedTest
        @MethodSource
        @DisplayName("Votes from ballot boxes get correctly returned by getVotes()")
        void votesGetCorrectlyCollectedAndReturned(
                List<BallotBox> ballotBoxes,
                Map<Vote, Integer> expectedVotes) {
            votingRound.setBallotBoxes(ballotBoxes);
            assertEquals(expectedVotes, votingRound.getVotes());
        }

        static Stream<Arguments> votesGetCorrectlyCollectedAndReturned() {
            Candidate[] candidates = ParametrisedUtil.getDifferenCandidates(3);

            // -1 means "create new ballot box
            // and add the following votes into it"
            // i (ie. 0 or more) means "add vote for candidate i"
            List<List<Integer>> ballotBoxesBlueprints = Arrays.asList(
                    Arrays.asList(),
                    Arrays.asList(-1),
                    Arrays.asList(-1, -1),
                    Arrays.asList(
                            -1,
                            -1, 0, 1, 1, 2),
                    // Tests that values from different ballot boxes
                    // under the same key add to each other,
                    // not replace each other:
                    Arrays.asList(
                            -1, 0, 1, 2,
                            -1, 0, 1, 2),
                    Arrays.asList(
                            -1, 0, 2,
                            -1, 2,
                            -1, 0, 1,
                            -1, 1, 1, 2, 2, 2));
            List<Map<Integer, Integer>> expectedVotesBlueprints = Arrays.asList(
                    Map.of(),
                    Map.of(),
                    Map.of(),
                    Map.of(
                            0, 1,
                            1, 2,
                            2, 1),
                    Map.of(
                            0, 2,
                            1, 2,
                            2, 2),
                    Map.of(
                            0, 2,
                            1, 3,
                            2, 5));

            List<List<BallotBox>> ballotBoxGroups = ParametrisedUtil
                    .getBallotBoxGroupsFromBlueprints(
                            ballotBoxesBlueprints, candidates);
            List<Map<Vote, Integer>> expectedVotesList = ParametrisedUtil
                    .getVotesListFromBlueprints(
                            expectedVotesBlueprints, candidates);

            return ParametrisedUtil
                    .multipleListsToStreamOfArguments(ballotBoxGroups, expectedVotesList);
        }
    }
}
