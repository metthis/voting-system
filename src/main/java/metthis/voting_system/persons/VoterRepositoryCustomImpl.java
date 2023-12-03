package metthis.voting_system.persons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.InvalidDataAccessApiUsageException;

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
                // Throws InvalidDataAccessApiUsageException because if it were to throw
                // a different exception type, Spring Data JPA would catch it and throw
                // InvalidDataAccessApiUsageException instead anyways.
                throw new InvalidDataAccessApiUsageException(
                        "Cannot count how many voted because some already voted in a following round.");
            }
            if (voter.getLastVotedRound() == votingRound) {
                count++;
            }
        }
        return count;
    }
}
