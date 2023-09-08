package metthis.voting_system.logic.elections;

import metthis.voting_system.domain.Voter;
import metthis.voting_system.domain.Candidate;

public class PresidentialElection extends AbstractElection implements Election {
    private final int MIN_VOTER_AGE = 18;
    private final int MIN_CANDIDATE_AGE = 40;

    public PresidentialElection(String electionDate) {
        super(electionDate);
    }

    public boolean isEligibleVoter(Voter voter) {
        return voter.getIsCitizen() &&
                voter.getAge(this.ELECTION_DATE) >= this.MIN_VOTER_AGE;
    }

    public boolean isEligibleCandidate(Candidate candidate) {
        return candidate.getIsCitizen() &&
                candidate.getAge(this.ELECTION_DATE) >= this.MIN_CANDIDATE_AGE;
    }
}
