package metthis.voting_system.elections;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRepository;
import metthis.voting_system.persons.VoterRepository;
import metthis.voting_system.voting.Vote;

public abstract class AbstractElectionRules implements ElectionRules {

    protected VoterRepository voterRepository;

    protected CandidateRepository candidateRepository;

    public AbstractElectionRules(VoterRepository voterRepository,
                                 CandidateRepository candidateRepository) {
        this.voterRepository = voterRepository;
        this.candidateRepository = candidateRepository;
    }

    public AbstractElectionRules() {
    }

    public boolean voteIsValid(Vote vote, ElectionInfo electionInfo) {
        Candidate choice = vote.getChoice();
        return choice != null && isEligibleCandidate(choice, electionInfo);
    }
}
