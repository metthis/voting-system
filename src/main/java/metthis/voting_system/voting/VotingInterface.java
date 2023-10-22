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

    // Can I avoid passing Election as a parameter?
    public boolean submitVote(Vote vote, Election election) {
        if (!election.isEligibleVoter(this.voter) || this.voter.getVoted()) {
            return false;
        }
        this.ballotBox.add(vote);
        this.voter.voted();
        return true;
    }
}
