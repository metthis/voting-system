package metthis.voting_system.voting;

import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface VoteRepository
        extends ListCrudRepository<Vote, UUID> {

}
