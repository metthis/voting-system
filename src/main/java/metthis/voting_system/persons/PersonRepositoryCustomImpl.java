package metthis.voting_system.persons;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

public abstract class PersonRepositoryCustomImpl<P extends Person> {
    @Autowired
    @Lazy
    PersonRepository<P> personRepository;

    private P getPersonById(P person) {
        return personRepository.findById(person.getID()).get();
    }

    public P addIfAbsent(P person) {
        P presentPerson = getPersonById(person);
        if (presentPerson == null) {
            personRepository.save(person);
            return null;
        }
        return presentPerson;
    }

    public P update(P person) {
        P presentPerson = getPersonById(person);
        if (presentPerson == null) {
            return null;
        }
        personRepository.save(person);
        return presentPerson;
    }

    public P addOrUpdate(P person) {
        P presentPerson = getPersonById(person);
        personRepository.save(person);
        return presentPerson;
    }

    public P remove(P person) {
        P presentPerson = getPersonById(person);
        if (presentPerson == null) {
            return null;
        }
        personRepository.delete(person);
        return presentPerson;
    }

    public void clear() {
        personRepository.deleteAll();
    }

    public boolean isEmpty() {
        return personRepository.count() == 0;
    }

    public boolean contains(P person) {
        return getPersonById(person) == person;
    }

    public Map<String, P> getRegister() {
        Map<String, P> map = new HashMap<>();
        for (P person : personRepository.findAll()) {
            map.put(person.getID(), person);
        }
        return map;
    }

    public int howManyRegistered() {
        return (int) personRepository.count();
    }
}
