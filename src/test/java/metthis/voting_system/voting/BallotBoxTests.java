package metthis.voting_system.voting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import metthis.voting_system.persons.Candidate;

public class BallotBoxTests {
    @Nested
    class New {
        @Test
        void isInstantiatedWithNew() {
            new BallotBox();
        }
    }

    @Nested
    class WhenNew {
        private BallotBox ballotBox;
        private Candidate defaultCandidate;

        @BeforeEach
        void initEach() {
            this.ballotBox = new BallotBox();
            this.defaultCandidate = new Candidate("name", "ID", "1960-01-01", true, "2020-01-01");
        }

        @Test
        void canAddVote() {
            Vote vote = new SingleCandidateSingleChoiceVote(defaultCandidate);
            this.ballotBox.add(vote);
        }

        @Test
        void canAddVoteWithCount() {
            Vote vote = new SingleCandidateSingleChoiceVote(defaultCandidate);
            this.ballotBox.add(vote, 2);
        }

        @Test
        void canAddEqualVotes() {
            Vote vote1 = new SingleCandidateSingleChoiceVote(defaultCandidate);
            Vote vote2 = new SingleCandidateSingleChoiceVote(defaultCandidate);
            this.ballotBox.add(vote1);
            this.ballotBox.add(vote2);
        }

        @Test
        void canGetVotes() {
            this.ballotBox.getVotes();
        }

        @Test
        void votesAreInitiallyEmpty() {
            assertEquals(0, this.ballotBox.getVotes().size());
        }

        @Test
        void addingVoteOnceMakesItCountOnce() {
            Vote vote = new SingleCandidateSingleChoiceVote(defaultCandidate);
            this.ballotBox.add(vote);
            assertEquals(1, this.ballotBox.getVotes().get(vote));
        }

        @ParameterizedTest
        @ValueSource(ints = { 1, 2, 3, 5, 20, 400, 7394 })
        void addingVoteWithCountMakesItCountThatManyTimes(int count) {
            Vote vote = new SingleCandidateSingleChoiceVote(defaultCandidate);
            this.ballotBox.add(vote, count);
            assertEquals(count, this.ballotBox.getVotes().get(vote));
        }

        @Test
        void addingVoteOnceResultsInOneEntry() {
            Vote vote = new SingleCandidateSingleChoiceVote(defaultCandidate);
            this.ballotBox.add(vote);
            assertEquals(1, this.ballotBox.getVotes().size());
        }

        @Test
        void addingTwoEqualVotesMakesItCountTwice() {
            Vote vote1 = new SingleCandidateSingleChoiceVote(defaultCandidate);
            Vote vote2 = new SingleCandidateSingleChoiceVote(defaultCandidate);
            this.ballotBox.add(vote1);
            this.ballotBox.add(vote2);
            assertEquals(2, this.ballotBox.getVotes().get(vote1));
        }

        @ParameterizedTest
        @ValueSource(ints = { 1, 2, 3, 5, 20, 400, 7394 })
        void addingTwoEqualVotesWithCountMakesItCountAppropriateNumberOfTimes(int count) {
            Vote vote1 = new SingleCandidateSingleChoiceVote(defaultCandidate);
            Vote vote2 = new SingleCandidateSingleChoiceVote(defaultCandidate);
            this.ballotBox.add(vote1, count);
            this.ballotBox.add(vote2, count);
            assertEquals(count * 2, this.ballotBox.getVotes().get(vote1));
        }

        @Test
        void addingTwoEqualVotesResultsInOneEntry() {
            Vote vote1 = new SingleCandidateSingleChoiceVote(defaultCandidate);
            Vote vote2 = new SingleCandidateSingleChoiceVote(defaultCandidate);
            this.ballotBox.add(vote1);
            this.ballotBox.add(vote2);
            assertEquals(1, this.ballotBox.getVotes().size());
        }

        @Test
        void addingTwoDifferentVotesResultsInTwoEntries() {
            Vote vote1 = new SingleCandidateSingleChoiceVote(defaultCandidate);

            Candidate candidate2 = new Candidate("name2", "ID2", "1961-01-01", true, "2020-01-01");
            Vote vote2 = new SingleCandidateSingleChoiceVote(candidate2);

            this.ballotBox.add(vote1);
            this.ballotBox.add(vote2);

            assertEquals(2, this.ballotBox.getVotes().size());
        }

        @Test
        void dumpIntoAndKeepKeepsDumpedBallotBoxContentUnchanged()
                throws NoSuchMethodException,
                InstantiationException,
                IllegalArgumentException,
                IllegalAccessException,
                InvocationTargetException {
            BallotBox giving = new BallotBox();
            BallotBox receiving = new BallotBox();

            Vote vote = new SingleCandidateSingleChoiceVote(defaultCandidate);
            giving.add(vote);
            Object givingContentBefore = cloneWithCopyConstructor(giving.getVotes());

            giving.dumpIntoAndKeep(receiving);

            assertEquals(givingContentBefore, giving.getVotes());
        }

        static Object cloneWithCopyConstructor(Object toClone)
                throws NoSuchMethodException,
                InstantiationException,
                IllegalArgumentException,
                IllegalAccessException,
                InvocationTargetException {
            Class<?> clazz = toClone.getClass();
            Constructor<?> copyConstructor = null;
            try {
                copyConstructor = clazz.getConstructor(clazz);
            } catch (NoSuchMethodException e1) {
                // Tries to get a copy constructor where the instance passed to it
                // is represented by one of the class' interfaces
                for (Class<?> inter : clazz.getInterfaces()) {
                    try {
                        copyConstructor = clazz.getConstructor(inter);
                        break;
                    } catch (NoSuchMethodException e2) {
                    }
                }
            }
            if (copyConstructor == null) {
                throw new NoSuchMethodException("No copy constructor was found");
            }
            return copyConstructor.newInstance(toClone);
        }

        @Test
        void dumpIntoAndKeepIntoEmptyBallotBoxResultsInBothHavingSameContents() {
            BallotBox giving = new BallotBox();
            BallotBox receiving = new BallotBox();

            Vote vote1 = new SingleCandidateSingleChoiceVote(defaultCandidate);
            Vote vote2 = new SingleCandidateSingleChoiceVote(defaultCandidate);
            giving.add(vote1);
            giving.add(vote2);

            giving.dumpIntoAndKeep(receiving);

            assertEquals(giving.getVotes(), receiving.getVotes());
        }

        @ParameterizedTest
        @MethodSource
        void dumpIntoAndKeepIntoNonEmptyBallotBoxAddsVotesCorrectly(
                BallotBox giving,
                BallotBox receiving,
                BallotBox expectedReceiving) {
            giving.dumpIntoAndKeep(receiving);
            assertEquals(expectedReceiving.getVotes(), receiving.getVotes());
        }

        static Stream<Arguments> dumpIntoAndKeepIntoNonEmptyBallotBoxAddsVotesCorrectly() {
            Candidate[] candidates = ParametrisedUtil.getDifferenCandidates(3);

            List<Map<Integer, Integer>> givingVotesBlueprints = Arrays.asList(
                    Map.of(),
                    Map.of(
                            0, 10,
                            2, 10),
                    Map.of(
                            0, 5,
                            1, 5,
                            2, 5),
                    Map.of(
                            0, 7,
                            1, 2,
                            2, 15));

            List<Map<Integer, Integer>> receivingVotesBlueprints = Arrays.asList(
                    Map.of(
                            0, 1,
                            1, 10,
                            2, 25),
                    Map.of(
                            0, 5,
                            1, 5,
                            2, 5),
                    Map.of(
                            0, 10,
                            2, 10),
                    Map.of(
                            0, 22,
                            1, 18,
                            2, 1));

            List<Map<Integer, Integer>> resultingVotesBlueprints = Arrays.asList(
                    Map.of(
                            0, 1,
                            1, 10,
                            2, 25),
                    Map.of(
                            0, 15,
                            1, 5,
                            2, 15),
                    Map.of(
                            0, 15,
                            1, 5,
                            2, 15),
                    Map.of(
                            0, 29,
                            1, 20,
                            2, 16));

            List<BallotBox> givingBallotBoxes = ParametrisedUtil
                    .getBallotBoxesFromBlueprints(
                            givingVotesBlueprints, candidates);
            List<BallotBox> receivingBallotBoxes = ParametrisedUtil
                    .getBallotBoxesFromBlueprints(
                            receivingVotesBlueprints, candidates);
            List<BallotBox> resultingBallotBoxes = ParametrisedUtil
                    .getBallotBoxesFromBlueprints(
                            resultingVotesBlueprints, candidates);

            return ParametrisedUtil
                    .multipleListsToStreamOfArguments(
                            givingBallotBoxes,
                            receivingBallotBoxes,
                            resultingBallotBoxes);
        }
    }
}
