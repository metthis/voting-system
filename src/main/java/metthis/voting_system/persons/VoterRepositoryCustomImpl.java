package metthis.voting_system.persons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import metthis.voting_system.elections.Election;

public class VoterRepositoryCustomImpl
        extends PersonRepositoryCustomImpl<Voter>
        implements VoterRepositoryCustom {

    @Autowired
    @Lazy
    VoterRepository voterRepository;

    @Override
    public int howManyEligible(Election election) {
        int count = 0;
        for (Voter voter : voterRepository.findAll()) {
            if (election.isEligibleVoter(voter)) {
                count++;
            }
        }
        return count;
    }

    public int howManyVoted(int votingRound) {
        int count = 0;
        for (Voter voter : voterRepository.findAll()) {
            if (voter.getLastVotedRound() > votingRound) {
                throw new IllegalArgumentException(
                        "Cannot count how many voted because some already voted in a following round.");
            }
            if (voter.getLastVotedRound() == votingRound) {
                count++;
            }
        }
        return count;
    }
}
