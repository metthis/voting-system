package metthis.voting_system.persons;

import java.util.HashMap;
import java.util.Map;

import metthis.voting_system.elections.Election;

public abstract class Register<P extends Person> {
    protected Map<String, P> register;
    protected Election election;

    public Register() {
        this.register = new HashMap<>();
    }

    public P addIfAbsent(P person) {
        return this.register.putIfAbsent(person.getID(), person);
    }

    public P update(P person) {
        return this.register.replace(person.getID(), person);
    }

    public P addOrUpdate(P person) {
        return this.register.put(person.getID(), person);
    }

    public P remove(P person) {
        return this.register.remove(person.getID());
    }

    public void clear() {
        this.register.clear();
    }

    public Map<String, P> getRegister() {
        return this.register;
    }

    public int howManyRegistered() {
        return this.register.size();
    }

    public int howManyEligible() {
        return (int) this.register.values().stream()
                .filter(person -> person.isEligible(this.election))
                .count();
    }
}
