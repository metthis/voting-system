package metthis.voting_system.persons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import metthis.voting_system.elections.Election;

public class CandidateRepositoryCustomImpl
        extends PersonRepositoryCustomImpl<Candidate>
        implements CandidateRepositoryCustom {

    @Autowired
    @Lazy
    CandidateRepository candidateRepository;

    @Override
    public int howManyEligible(Election election) {
        int count = 0;
        for (Candidate candidate : candidateRepository.findAll()) {
            if (election.isEligibleCandidate(candidate)) {
                count++;
            }
        }
        return count;
    }

    public int howManyWithdrew() {
        int count = 0;
        for (Candidate candidate : candidateRepository.findAll()) {
            if (candidate.getWithdrawalDate() != null) {
                count++;
            }
        }
        return count;
    }
}
