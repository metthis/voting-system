package metthis.voting_system.persons;

import metthis.voting_system.elections.Election;

public class VoterRegister extends Register<Voter> {
    public VoterRegister() {
        super();
    }

    @Override
    public int howManyEligible(Election election) {
        return (int) this.register.values().stream()
                .filter(voter -> election.isEligibleVoter(voter))
                .count();
    }

    public int howManyVoted() {
        return (int) this.register.values().stream()
                .filter(voter -> voter.getVoted())
                .count();
    }
}
