package metthis.voting_system.elections;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRepository;
import metthis.voting_system.persons.Voter;
import metthis.voting_system.persons.VoterRepository;
import org.springframework.stereotype.Service;

@Service
public class PresidentialElectionRules extends AbstractElectionRules {

    public PresidentialElectionRules(VoterRepository voterRepository,
                                     CandidateRepository candidateRepository) {
        super(voterRepository, candidateRepository);
    }

    public boolean isEligibleVoter(Voter voter, ElectionInfo electionInfo) {
        final int MIN_VOTER_AGE = 18;
        return voter.getIsCitizen() &&
                voter.getAge(electionInfo.getDate()) >= MIN_VOTER_AGE &&
                voterRepository.exists(voter);
    }

    public boolean isEligibleCandidate(Candidate candidate, ElectionInfo electionInfo) {
        final int MIN_CANDIDATE_AGE = 40;
        return candidate.getIsCitizen() &&
                candidate.getAge(electionInfo.getDate()) >= MIN_CANDIDATE_AGE &&
                candidateRepository.exists(candidate) &&
                candidate.getWithdrawalDate() == null &&
                !candidate.getLostThisElection();
    }
}
