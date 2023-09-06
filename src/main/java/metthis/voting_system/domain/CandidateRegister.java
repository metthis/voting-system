package metthis.voting_system.domain;

public class CandidateRegister extends Register<Candidate> {
    public CandidateRegister() {
        super();
    }

    @Override
    public boolean isEligible(Candidate candidate) {
        return this.votingRound.isEligibleCandidate(candidate);
    }

    public int howManyWithdrew() {
        return (int) this.register.values().stream()
                .filter(candidate -> candidate.getWithdrawalDate() != null)
                .count();
    }
}
