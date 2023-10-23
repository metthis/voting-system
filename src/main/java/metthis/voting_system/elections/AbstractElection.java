package metthis.voting_system.elections;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRegister;
import metthis.voting_system.persons.VoterRegister;
import metthis.voting_system.voting.Vote;
import metthis.voting_system.voting.VotingRound;

/*
 * Stores eligibility criteria
 * Creates a voting round which collects votes according to criteria
 * Uses votes to advance the election
 *  - Creates another round if it's needed
 *  - Saves the result when the election is over
 */

public abstract class AbstractElection implements Election {
    protected final LocalDate ELECTION_DATE;
    protected VoterRegister voters;
    protected CandidateRegister candidates;
    protected List<VotingRound> votingRounds;

    public AbstractElection(String electionDate) {
        this.ELECTION_DATE = LocalDate.parse(electionDate);
        this.voters = new VoterRegister();
        this.candidates = new CandidateRegister();
        this.votingRounds = new ArrayList<>(4);
    }

    public VoterRegister getVoters() {
        return this.voters;
    }

    public CandidateRegister getCandidates() {
        return this.candidates;
    }

    public List<VotingRound> getVotingRounds() {
        return this.votingRounds;
    }

    public VotingRound newVotingRound() {
        VotingRound newRound = new VotingRound();
        this.votingRounds.add(newRound);
        return newRound;
    }

    public boolean voteIsValid(Vote vote) {
        Candidate choice = vote.getChoice();
        return choice != null && isEligibleCandidate(choice);
    }
}
