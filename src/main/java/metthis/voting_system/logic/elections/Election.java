package metthis.voting_system.logic.elections;

import metthis.voting_system.domain.Voter;
import metthis.voting_system.domain.Candidate;

public interface Election {
    boolean isEligibleVoter(Voter voter);

    boolean isEligibleCandidate(Candidate candidate);
}
