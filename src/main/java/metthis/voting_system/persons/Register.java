package metthis.voting_system.persons;

import java.util.HashMap;
import java.util.Map;

import metthis.voting_system.elections.Election;

public abstract class Register<P extends Person> {
    protected Map<String, P> register;

    public Register() {
        this.register = new HashMap<>();
    }

    public P addIfAbsent(P person) {
        return this.register.putIfAbsent(person.getId(), person);
    }

    public P update(P person) {
        return this.register.replace(person.getId(), person);
    }

    public P addOrUpdate(P person) {
        return this.register.put(person.getId(), person);
    }

    public P remove(P person) {
        return this.register.remove(person.getId());
    }

    public void clear() {
        this.register.clear();
    }

    public boolean isEmpty() {
        return this.register.isEmpty();
    }

    public boolean contains(P person) {
        P containedPerson = this.register.get(person.getId());
        return containedPerson == person;
    }

    public Map<String, P> getRegister() {
        return this.register;
    }

    public int howManyRegistered() {
        return this.register.size();
    }

    public abstract int howManyEligible(Election election);
}
