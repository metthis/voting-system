package metthis.voting_system.elections;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.Voter;
import metthis.voting_system.voting.Vote;

public interface ElectionRules {
    boolean isEligibleVoter(Voter voter, ElectionInfo electionInfo);

    boolean isEligibleCandidate(Candidate candidate, ElectionInfo electionInfo);

    boolean voteIsValid(Vote vote, ElectionInfo electionInfo);
}
