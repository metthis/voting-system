package metthis.voting_system.logic.elections;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

import metthis.voting_system.domain.VoterRegister;
import metthis.voting_system.domain.CandidateRegister;
import metthis.voting_system.logic.voting.VotingRound;

/*
 * Stores eligibility criteria
 * Creates a voting round which collects votes according to criteria
 * Uses votes to advance the election
 *  - Creates another round if it's needed
 *  - Saves the result when the election is over
 */

public abstract class AbstractElection {
    protected final LocalDate ELECTION_DATE;
    protected VoterRegister voters;
    protected CandidateRegister candidates;
    protected List<VotingRound> rounds;

    public AbstractElection(String electionDate) {
        this.ELECTION_DATE = LocalDate.parse(electionDate);
        this.voters = new VoterRegister();
        this.candidates = new CandidateRegister();
        this.rounds = new ArrayList<>(4);
    }
}
