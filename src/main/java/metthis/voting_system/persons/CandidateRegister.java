package metthis.voting_system.persons;

import metthis.voting_system.elections.Election;

public class CandidateRegister extends Register<Candidate> {
    public CandidateRegister() {
        super();
    }

    @Override
    public int howManyEligible(Election election) {
        return (int) this.register.values().stream()
                .filter(candidate -> election.isEligibleCandidate(candidate))
                .count();
    }

    public int howManyWithdrew() {
        return (int) this.register.values().stream()
                .filter(candidate -> candidate.getWithdrawalDate() != null)
                .count();
    }
}
