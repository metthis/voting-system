package metthis.voting_system.persons;

public interface VoterRepositoryCustom
        extends PersonRepositoryCustom<Voter> {
    int howManyVoted(int votingRound);
}
