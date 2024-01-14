package metthis.voting_system.voting;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.UUID;

public interface VoteRepository
        extends ListCrudRepository<Vote, UUID>, ListPagingAndSortingRepository<Vote, UUID> {

}
