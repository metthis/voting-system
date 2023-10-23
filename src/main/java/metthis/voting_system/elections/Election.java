package metthis.voting_system.elections;

import java.util.List;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRegister;
import metthis.voting_system.persons.Voter;
import metthis.voting_system.persons.VoterRegister;
import metthis.voting_system.voting.Vote;
import metthis.voting_system.voting.VotingRound;

public interface Election {
    boolean isEligibleVoter(Voter voter);

    boolean isEligibleCandidate(Candidate candidate);

    boolean voteIsValid(Vote vote);

    VoterRegister getVoters();

    CandidateRegister getCandidates();

    List<VotingRound> getVotingRounds();

    VotingRound newVotingRound();
}
