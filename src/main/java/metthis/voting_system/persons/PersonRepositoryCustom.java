package metthis.voting_system.persons;

import java.util.Map;

import org.springframework.data.repository.NoRepositoryBean;

import metthis.voting_system.elections.Election;

@NoRepositoryBean
public interface PersonRepositoryCustom<P extends Person> {
    P addIfAbsent(P person);

    P update(P person);

    P addOrUpdate(P person);

    P remove(P person);

    void clear();

    boolean isEmpty();

    boolean contains(P person);

    Map<String, P> getRegister();

    int howManyRegistered();

    int howManyEligible(Election election);
}
