package metthis.voting_system.persons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

public abstract class PersonRepositoryCustomImpl<P extends Person> {
    @Autowired
    @Lazy
    PersonRepository<P> personRepository;

    public boolean isEmpty() {
        return personRepository.count() == 0;
    }

    public boolean exists(P person) {
        return personRepository.existsById(person.getId());
    }
}
