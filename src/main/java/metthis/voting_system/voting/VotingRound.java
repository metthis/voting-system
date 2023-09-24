package metthis.voting_system.voting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VotingRound {
    private List<BallotBox> ballotBoxes;

    public VotingRound() {
        this.ballotBoxes = new ArrayList<>();
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

    public Map<Vote, Integer> getVotes() {
        BallotBox allVotes = new BallotBox();
        for (BallotBox ballotBox : this.ballotBoxes) {
            ballotBox.dumpIntoAndKeep(allVotes);
        }
        return allVotes.getVotes();
    }
}