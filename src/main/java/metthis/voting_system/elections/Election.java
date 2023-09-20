package metthis.voting_system.elections;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.Voter;
import metthis.voting_system.persons.VoterRegister;

public interface Election {
    boolean isEligibleVoter(Voter voter);

    boolean isEligibleCandidate(Candidate candidate);

    VoterRegister getVoters();
}
