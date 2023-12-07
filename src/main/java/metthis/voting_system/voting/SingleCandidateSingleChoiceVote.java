package metthis.voting_system.voting;

import jakarta.persistence.Entity;
import metthis.voting_system.persons.Candidate;

@Entity
public class SingleCandidateSingleChoiceVote extends Vote {
    public SingleCandidateSingleChoiceVote(Integer votingRound, Candidate choice) {
        super(votingRound, choice);
    }

    public SingleCandidateSingleChoiceVote(Integer votingRound) {
        super(votingRound);
    }

    // To be removed when the old implementation is removed
    public SingleCandidateSingleChoiceVote(Candidate choice) {
        super(choice);
    }

    public SingleCandidateSingleChoiceVote() {
        super();
    }
}
