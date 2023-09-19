package metthis.voting_system.voting;

import java.util.HashMap;
import java.util.Map;

public class BallotBox {
    private Map<Vote, Integer> votes;

    public BallotBox() {
        this.votes = new HashMap<>();
    }

    // Later: Should encrypt the vote.
    public void add(Vote vote) {
        int alreadySubmitted = this.votes.getOrDefault(vote, 0);
        this.votes.put(vote, alreadySubmitted + 1);
    }

    // Later: Should provide a map of decrypted votes.
    public Map<Vote, Integer> getVotes() {
        return this.votes;
    }
}