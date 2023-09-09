package metthis.voting_system.persons;

public class VoterRegister extends Register<Voter> {
    public VoterRegister() {
        super();
    }

    public int howManyVoted() {
        return (int) this.register.values().stream()
                .filter(voter -> voter.getVoted())
                .count();
    }
}
