package metthis.voting_system.voting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
    }
}
