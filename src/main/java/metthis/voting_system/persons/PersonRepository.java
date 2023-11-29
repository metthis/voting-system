package metthis.voting_system.persons;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PersonRepository<P extends Person>
        extends CrudRepository<P, String> {

}
