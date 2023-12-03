package metthis.voting_system.persons;

import org.springframework.data.repository.NoRepositoryBean;

import metthis.voting_system.elections.Election;

@NoRepositoryBean
public interface PersonRepositoryCustom<P extends Person> {
    boolean isEmpty();

    boolean exists(P person);

    int howManyEligible(Election election);
}
