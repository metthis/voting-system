package metthis.voting_system.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import metthis.voting_system.elections.Election;
import metthis.voting_system.persons.Candidate;
import metthis.voting_system.voting.Vote;
import metthis.voting_system.voting.VotingRound;

public class VotingRoundEvaluator {
    private final Map<Vote, Integer> allVotes;
    private final Map<Vote, Integer> validVotes;
    private final SortedMap<Integer, Vote[]> validVotesByCount;
    private final int allVotesCount;
    private final int validVotesCount;

    public VotingRoundEvaluator(VotingRound round, Election election) {
        this.allVotes = round.getVotes();
        this.validVotes = this.initValidVotes(election);
        this.validVotesByCount = this.initValidVotesByCount();
        this.allVotesCount = this.initVotesCount(this.allVotes);
        this.validVotesCount = this.initVotesCount(this.validVotes);
    }

    private Map<Vote, Integer> initValidVotes(Election election) {
        return this.allVotes.entrySet().stream()
                .filter(entry -> election.voteIsValid(entry.getKey()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    private SortedMap<Integer, Vote[]> initValidVotesByCount() {
        return this.validVotes.entrySet().stream()
                .collect(Collectors.toMap(
                        Entry::getValue,
                        entry -> multimapValue(entry.getKey()),
                        (v1, v2) -> mergeMultimapValues(v1, v2),
                        TreeMap::new));
    }

    private static Vote[] multimapValue(Vote value) {
        return new Vote[] { value };
    }

    private static Vote[] mergeMultimapValues(Vote[] base, Vote[] added) {
        Vote[] newValue = Arrays.copyOf(base, base.length + added.length);
        for (int i = 0; i < added.length; i++) {
            newValue[base.length + i] = added[i];
        }
        return newValue;
    }

    private int initVotesCount(Map<Vote, Integer> votes) {
        return votes.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getAllVotesCount() {
        return this.allVotesCount;
    }

    public int getValidVotesCount() {
        return this.validVotesCount;
    }

    public Candidate[][] getRoundWinners(int usualNumberOfWinners) {
        if (usualNumberOfWinners <= 0) {
            return new Candidate[0][];
        }

        // A copy of validVotesByCount is used because this method would mutate it.
        SortedMap<Integer, Vote[]> votes = new TreeMap<>(this.validVotesByCount);
        List<Candidate[]> winners = new ArrayList<>();

        while (!votes.isEmpty()) {
            int highestRemainingVoteCount = votes.lastKey();
            Vote[] nextWinnerVotes = votes.remove(highestRemainingVoteCount);
            Candidate[] nextWinners = Arrays.stream(nextWinnerVotes)
                    .map(vote -> vote.getChoice())
                    .toArray(Candidate[]::new);
            winners.add(nextWinners);

            int currentNumberOfWinners = winners.stream()
                    .mapToInt(array -> array.length)
                    .sum();
            if (currentNumberOfWinners >= usualNumberOfWinners) {
                break;
            }
        }
        return winners.toArray(Candidate[][]::new);
    }
}
