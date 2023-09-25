package metthis.voting_system.elections;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.Voter;

public class PresidentialElection extends AbstractElection {
    private final int MIN_VOTER_AGE = 18;
    private final int MIN_CANDIDATE_AGE = 40;

    public PresidentialElection(String electionDate) {
        super(electionDate);
    }

    public boolean isEligibleVoter(Voter voter) {
        return voter.getIsCitizen() &&
                voter.getAge(this.ELECTION_DATE) >= this.MIN_VOTER_AGE &&
                this.voters.contains(voter);
    }

    public boolean isEligibleCandidate(Candidate candidate) {
        return candidate.getIsCitizen() &&
                candidate.getAge(this.ELECTION_DATE) >= this.MIN_CANDIDATE_AGE &&
                this.candidates.contains(candidate) &&
                candidate.getLostThisElection() == false;
    }
}
