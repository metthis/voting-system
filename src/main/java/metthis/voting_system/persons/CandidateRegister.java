package metthis.voting_system.persons;

public class CandidateRegister extends Register<Candidate> {
    public CandidateRegister() {
        super();
    }

    public int howManyWithdrew() {
        return (int) this.register.values().stream()
                .filter(candidate -> candidate.getWithdrawalDate() != null)
                .count();
    }
}
