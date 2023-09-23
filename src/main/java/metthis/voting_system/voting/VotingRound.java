package metthis.voting_system.voting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VotingRound {
    private List<BallotBox> ballotBoxes;
    private Map<Vote, Integer> votes;

    public VotingRound() {
        this.ballotBoxes = new ArrayList<>();
        this.votes = null;
    }

    public BallotBox createBallotBox() {
        BallotBox ballotBox = new BallotBox();
        this.ballotBoxes.add(ballotBox);
        return ballotBox;
    }

    public List<BallotBox> getBallotBoxes() {
        return this.ballotBoxes;
    }

    // For unit tests only:
    void setBallotBoxes(List<BallotBox> ballotBoxes) {
        this.ballotBoxes = ballotBoxes;
    }

    public void collectVotesFromBallotBoxes() {
        BallotBox allVotes = new BallotBox();
        for (BallotBox ballotBox : this.ballotBoxes) {
            ballotBox.dumpIntoAndKeep(allVotes);
        }
        this.votes = allVotes.getVotes();
    }

    public Map<Vote, Integer> getVotes() {
        return this.votes;
    }
}