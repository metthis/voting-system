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
        this.add(vote, 1);
    }

    public void add(Vote vote, int count) {
        int alreadySubmitted = this.votes.getOrDefault(vote, 0);
        this.votes.put(vote, alreadySubmitted + count);
    }

    // Later: Should provide a map of decrypted votes.
    public Map<Vote, Integer> getVotes() {
        return this.votes;
    }

    public void dumpIntoAndKeep(BallotBox other) {
        for (Map.Entry<Vote, Integer> entry : this.votes.entrySet()) {
            other.add(entry.getKey(), entry.getValue());
        }
    }
}