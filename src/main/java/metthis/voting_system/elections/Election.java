package metthis.voting_system.elections;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.Voter;

public interface Election {
    boolean isEligibleVoter(Voter voter);

    boolean isEligibleCandidate(Candidate candidate);
}
