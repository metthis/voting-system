package metthis.voting_system.voting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import metthis.voting_system.persons.Candidate;

public class SingleCandidateSingleChoiceVoteTests {
    @Nested
    class TestsWhichUseDefaultCandidate {
        private Candidate defaulCandidate;

        @BeforeEach
        void initEach() {
            this.defaulCandidate = new Candidate("name", "ID", "1960-01-01", true, "2020-01-01");
        }

        @Test
        void canGetChoice() {
            SingleCandidateSingleChoiceVote vote = new SingleCandidateSingleChoiceVote(
                    this.defaulCandidate);
            Candidate actual = vote.getChoice();
            assertEquals(this.defaulCandidate, actual);
        }

        @Test
        void getChoiceAfterParameterlessConstructorReturnsNull() {
            SingleCandidateSingleChoiceVote vote = new SingleCandidateSingleChoiceVote();
            Candidate actual = vote.getChoice();
            assertNull(actual);
        }

        @Test
        void votesWithSameChoiceAreEqualAndHaveSameHashCode() {
            SingleCandidateSingleChoiceVote vote1 = new SingleCandidateSingleChoiceVote(
                    this.defaulCandidate);
            SingleCandidateSingleChoiceVote vote2 = new SingleCandidateSingleChoiceVote(
                    this.defaulCandidate);

            assertEquals(vote1, vote2);
            assertEquals(vote1.hashCode(), vote2.hashCode());
        }
    }

    @Nested
    class EqualityAndHashCodeTests {
        @Test
        void votesWithEqualChoiceAreEqualAndHaveSameHashCode() {
            Candidate candidate1 = new Candidate("name", "ID", "1960-01-01", true, "2020-01-01");
            Candidate candidate2 = new Candidate("name", "ID", "1960-01-01", true, "2020-01-01");

            // These 2 candidates must be considered equal
            // for this test's premise to be true
            assertEquals(candidate1, candidate2);

            SingleCandidateSingleChoiceVote vote1 = new SingleCandidateSingleChoiceVote(candidate1);
            SingleCandidateSingleChoiceVote vote2 = new SingleCandidateSingleChoiceVote(candidate2);

            assertEquals(vote1, vote2);
            assertEquals(vote1.hashCode(), vote2.hashCode());
        }

        @Test
        void votesWithNullChoiceAreEqualAndHaveSameHashCode() {
            SingleCandidateSingleChoiceVote vote1 = new SingleCandidateSingleChoiceVote();
            SingleCandidateSingleChoiceVote vote2 = new SingleCandidateSingleChoiceVote();

            assertEquals(vote1, vote2);
            assertEquals(vote1.hashCode(), vote2.hashCode());
        }
    }
}
