package metthis.voting_system.persons;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PersonRepository<P extends Person>
        extends ListCrudRepository<P, String> {

}
