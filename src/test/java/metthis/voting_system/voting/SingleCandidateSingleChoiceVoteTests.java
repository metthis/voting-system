package metthis.voting_system.voting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import metthis.voting_system.persons.Candidate;

public class SingleCandidateSingleChoiceVoteTests {
    @Nested
    class New {
        @Test
        void isInitializedWithDefaultConstructor() {
            new SingleCandidateSingleChoiceVote();
        }

        @Test
        void isInitializedWithConstructorWithVotingRoundArgument() {
            new SingleCandidateSingleChoiceVote(1);
        }

        @Test
        void isInitializedWithConstructorWithVotingRoundAndChoiceArguments() {
            Candidate candidate = new Candidate("name", "ID", "1960-01-01", true, "2020-01-01");
            new SingleCandidateSingleChoiceVote(1, candidate);
        }
    }

    @Nested
    class TestsWhichUseDefaultCandidate {
        private Candidate defaulCandidate;

        @BeforeEach
        void initEach() {
            this.defaulCandidate = new Candidate("name", "ID", "1960-01-01", true, "2020-01-01");
        }

        @Test
        void canGetVotingRound() {
            SingleCandidateSingleChoiceVote vote = new SingleCandidateSingleChoiceVote(
                    1,
                    this.defaulCandidate);
            Integer actual = vote.getVotingRound();
            assertEquals(1, actual);
        }

        @Test
        void canGetChoice() {
            SingleCandidateSingleChoiceVote vote = new SingleCandidateSingleChoiceVote(
                    1,
                    this.defaulCandidate);
            Candidate actual = vote.getChoice();
            assertEquals(this.defaulCandidate, actual);
        }

        @Test
        void getChoiceAfterConstructorWithOnlyTheVotingRoundArgumentReturnsNull() {
            SingleCandidateSingleChoiceVote vote = new SingleCandidateSingleChoiceVote(1);
            Candidate actual = vote.getChoice();
            assertNull(actual);
        }
    }

    @Nested
    class EqualityAndHashCodeTests {
        @Test
        void votesWithEqualVotingRoundAndEqualChoiceAreEqualAndHaveSameHashCode() {
            Candidate candidate1 = new Candidate("name", "ID", "1960-01-01", true, "2020-01-01");
            Candidate candidate2 = new Candidate("name", "ID", "1960-01-01", true, "2020-01-01");

            // These 2 candidates must be considered equal
            // for this test's premise to be true
            assertEquals(candidate1, candidate2);

            SingleCandidateSingleChoiceVote vote1 = new SingleCandidateSingleChoiceVote(1, candidate1);
            SingleCandidateSingleChoiceVote vote2 = new SingleCandidateSingleChoiceVote(1, candidate2);

            assertEquals(vote1, vote2);
            assertEquals(vote1.hashCode(), vote2.hashCode());
        }

        @Test
        void votesWithEqualVotingRoundAndNullChoiceAreEqualAndHaveSameHashCode() {
            SingleCandidateSingleChoiceVote vote1 = new SingleCandidateSingleChoiceVote();
            SingleCandidateSingleChoiceVote vote2 = new SingleCandidateSingleChoiceVote();

            assertEquals(vote1, vote2);
            assertEquals(vote1.hashCode(), vote2.hashCode());
        }

        @Test
        void votesWithDifferentVotingRoundAreNotEqual() {
            Candidate candidate1 = new Candidate("name", "ID", "1960-01-01", true, "2020-01-01");
            Candidate candidate2 = new Candidate("name", "ID", "1960-01-01", true, "2020-01-01");

            // These 2 candidates must be considered equal
            // to isolate the influence of votingRound
            assertEquals(candidate1, candidate2);

            SingleCandidateSingleChoiceVote vote1 = new SingleCandidateSingleChoiceVote(1, candidate1);
            SingleCandidateSingleChoiceVote vote2 = new SingleCandidateSingleChoiceVote(2, candidate2);

            assertNotEquals(vote1, vote2);
        }

        @Test
        void votesWithDifferentChoiceAreNotEqual() {
            Candidate candidate1 = new Candidate("name1", "ID1", "1960-01-01", true, "2020-01-01");
            Candidate candidate2 = new Candidate("name2", "ID2", "1980-01-01", false, "2022-01-01");

            // These 2 candidates must be considered not equal
            // for this test's premise to be true
            assertNotEquals(candidate1, candidate2);

            SingleCandidateSingleChoiceVote vote1 = new SingleCandidateSingleChoiceVote(1, candidate1);
            SingleCandidateSingleChoiceVote vote2 = new SingleCandidateSingleChoiceVote(1, candidate2);

            assertNotEquals(vote1, vote2);
        }
    }
}
