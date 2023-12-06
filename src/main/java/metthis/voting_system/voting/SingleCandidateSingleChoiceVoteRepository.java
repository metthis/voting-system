package metthis.voting_system.voting;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface SingleCandidateSingleChoiceVoteRepository
        extends CrudRepository<SingleCandidateSingleChoiceVote, UUID> {

}
