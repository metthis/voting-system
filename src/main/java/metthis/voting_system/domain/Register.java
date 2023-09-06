package metthis.voting_system.domain;

import java.util.Map;
import java.util.HashMap;

public abstract class Register<P extends Person> {
    protected Map<String, P> register;
    protected VotingRound votingRound;

    public Register() {
        this.register = new HashMap<>();
    }

    public Person addIfAbsent(P person) {
        return this.register.putIfAbsent(person.getID(), person);
    }

    public Person replace(P person) {
        return this.register.replace(person.getID(), person);
    }

    public Person addOrReplace(P person) {
        return this.register.put(person.getID(), person);
    }

    public Person remove(P person) {
        return this.register.remove(person.getID());
    }

    public abstract boolean isEligible(P person);

    public Map<String, P> getRegister() {
        return this.register;
    }

    public int howManyRegistered() {
        return this.register.size();
    }

    public int howManyEligible() {
        return (int) this.register.values().stream()
                .filter(person -> this.isEligible(person))
                .count();
    }
}
