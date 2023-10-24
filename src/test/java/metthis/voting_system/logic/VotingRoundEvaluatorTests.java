package metthis.voting_system.logic;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

import metthis.voting_system.elections.Election;
import metthis.voting_system.elections.PresidentialElection;
import metthis.voting_system.persons.Candidate;
import metthis.voting_system.voting.ParametrisedUtil;
import metthis.voting_system.voting.Vote;
import metthis.voting_system.voting.VotingRound;

public class VotingRoundEvaluatorTests {
    protected Election election;

    @BeforeEach
    void initElection() {
        this.election = new PresidentialElection("2020-01-01");
    }

    @Nested
    class New {
        private VotingRound emptyRound;

        @BeforeEach
        void initEmptyRound() {
            this.emptyRound = new VotingRound();
        }

        @Test
        void isInstantiatedWithNew() {
            new VotingRoundEvaluator(emptyRound, election);
        }

        @Nested
        class WhenNew {
            private VotingRoundEvaluator evaluator;

            @BeforeEach
            void initEvaluator() {
                evaluator = new VotingRoundEvaluator(emptyRound, election);
            }

            @Test
            void canGetAllVotesCound() {
                evaluator.getAllVotesCount();
            }

            @Test
            void canGetValidVotesCound() {
                evaluator.getValidVotesCount();
            }

            @Test
            void canGetRoundWinners() {
                evaluator.getRoundWinners(0);
            }
        }
    }

    @Nested
    class ParametrisedInitialisation {
        @ParameterizedTest
        @MethodSource
        void getAllVotesCountReturnsCorrectly(VotingRound round, int expected) {
            VotingRoundEvaluator evaluator = new VotingRoundEvaluator(round, election);
            assertEquals(expected, evaluator.getAllVotesCount());
        }

        static Stream<Arguments> getAllVotesCountReturnsCorrectly() {
            Candidate[] candidates = ParametrisedUtil.getDifferenCandidates(2);

            List<Map<Integer, Integer>> roundBlueprints = Arrays.asList(
                    Map.of(),
                    Map.of(
                            0, 1),
                    Map.of(
                            0, 1,
                            1, 4));

            List<Integer> expectedList = Arrays.asList(0, 1, 5);

            List<VotingRound> rounds = ParametrisedUtil
                    .getVotingRoundsFromBlueprints(roundBlueprints, candidates);

            return ParametrisedUtil.multipleListsToStreamOfArguments(
                    rounds, expectedList);
        }

        @ParameterizedTest
        @MethodSource
        void getValidVotesCountReturnsCorrectly(VotingRound round, int expected) {
            // Candidate with ID "0" won't be registered in the election
            // and will thus be ineligible:
            this.registerCandidatesIntoElection(round, election, "0");
            VotingRoundEvaluator evaluator = new VotingRoundEvaluator(round, election);
            assertEquals(expected, evaluator.getValidVotesCount());
        }

        void registerCandidatesIntoElection(
                VotingRound round,
                Election election,
                String nonRegisteredCandidateId) {
            assertTrue(election.getCandidates().getRegister().isEmpty());
            for (Vote vote : round.getVotes().keySet()) {
                Candidate candidate = vote.getChoice();
                if (candidate == null ||
                        candidate.getID().equals(nonRegisteredCandidateId)) {
                    continue;
                }
                election.getCandidates().addIfAbsent(candidate);
            }
            for (Candidate candidate : election.getCandidates().getRegister().values()) {
                assertNotEquals(nonRegisteredCandidateId, candidate.getID(),
                        "Registered candidates: " + election.getCandidates()
                                .getRegister().size());
            }
        }

        static Stream<Arguments> getValidVotesCountReturnsCorrectly() {
            Candidate[] candidates = ParametrisedUtil.getDifferenCandidates(3);

            // Candidate with key 0 is ineligible,
            // see method registerCandidatesOtherThan0IntoElection above
            List<Map<Integer, Integer>> roundBlueprints = Arrays.asList(
                    Map.of(
                            0, 10),
                    Map.of(
                            0, 10,
                            1, 1),
                    Map.of(
                            0, 10,
                            1, 1,
                            2, 4));

            List<Integer> expectedList = Arrays.asList(0, 1, 5);

            List<VotingRound> rounds = ParametrisedUtil
                    .getVotingRoundsFromBlueprints(roundBlueprints, candidates);

            return ParametrisedUtil.multipleListsToStreamOfArguments(
                    rounds, expectedList);
        }

        @ParameterizedTest
        @ArgumentsSource(GetRoundWinnersArgumentsProvider.class)
        void getRoundWinnersReturnsCorrectly(
                VotingRound round,
                int usualNumberOfWinners,
                Candidate[][] expected) {
            this.registerCandidatesIntoElection(round, election, "5");
            VotingRoundEvaluator evaluator = new VotingRoundEvaluator(round, election);
            Candidate[][] actual = evaluator.getRoundWinners(usualNumberOfWinners);
            assertEquals(expected.length, actual.length);
            for (int i = 0; i < expected.length; i++) {
                Arrays.sort(expected[i]);
                Arrays.sort(actual[i]);
                assertArrayEquals(expected[i], actual[i]);
            }
        }

        @ParameterizedTest
        @ArgumentsSource(GetNumberOfWinnersArgumentsProvider.class)
        void getNumberOfWinnersReturnsCorrectly(int expected, Candidate[][] winners) {
            assertEquals(expected, VotingRoundEvaluator.getNumberOfWinners(winners));
        }
    }
}
