package metthis.voting_system.voting;

import metthis.voting_system.elections.Election;
import metthis.voting_system.persons.Voter;

public class VotingInterface {
    private final BallotBox ballotBox;
    private final Voter voter;

    public VotingInterface(BallotBox ballotBox, Voter voter) {
        this.ballotBox = ballotBox;
        this.voter = voter;
    }

    public boolean submitVote(Vote vote, Election election) {
        int totalVotingRounds = election.getVotingRounds().size();

        // Fails when voter isn't eligible or when they already
        // voted in this round.
        if (!election.isEligibleVoter(this.voter) ||
        // Assumes that the vote is always being submitted into
        // the most recent voting round.
                this.voter.getLastVotedRound() >= totalVotingRounds) {
            return false;
        }

        this.ballotBox.add(vote);
        // Assumes that the vote is always being submitted into
        // the most recent voting round.
        this.voter.setLastVotedRound(totalVotingRounds);
        return true;
    }
}
