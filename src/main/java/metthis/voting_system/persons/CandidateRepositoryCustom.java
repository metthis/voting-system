package metthis.voting_system.persons;

public interface CandidateRepositoryCustom
        extends PersonRepositoryCustom<Candidate> {
    int howManyWithdrew();
}
