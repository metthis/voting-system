package metthis.voting_system.domain;

import java.util.Map;

public class VoterRegister extends Register<Voter> {
    public VoterRegister() {
        super();
    }

    @Override
    public boolean isEligible(Voter voter) {
        return false; // Placeholder until I can implement the line below.
        // return this.votingRound.isEligibleVoter(voter);
    }

    public int howManyVoted() {
        return (int) this.register.values().stream()
                .filter(voter -> voter.getVoted())
                .count();
    }
}
