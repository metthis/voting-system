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

    public int howManyVoted(int votingRound) {
        for (Voter voter : this.register.values()) {
            if (voter.getLastVotedRound() > votingRound) {
                throw new IllegalArgumentException(
                        "Cannot count how many voted because some already voted in a following round.");
            }
        }

        return (int) this.register.values().stream()
                .filter(voter -> voter.getLastVotedRound() == votingRound)
                .count();
    }
}
