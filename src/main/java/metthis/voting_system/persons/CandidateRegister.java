package metthis.voting_system.persons;

public class CandidateRegister extends Register<Candidate> {
    public CandidateRegister() {
        super();
    }

    @Override
    public boolean isEligible(Candidate candidate) {
        return false; // Placeholder until I can implement the line below.
        // return this.votingRound.isEligibleCandidate(candidate);
    }

    public int howManyWithdrew() {
        return (int) this.register.values().stream()
                .filter(candidate -> candidate.getWithdrawalDate() != null)
                .count();
    }
}
