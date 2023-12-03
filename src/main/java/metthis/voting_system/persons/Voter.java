package metthis.voting_system.persons;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Voter extends Person {

    @Column(nullable = false)
    private int lastVotedRound;

    public Voter() {
        super();
    }

    public Voter(String name, String ID, String dateOfBirth, boolean isCitizen) {
        super(name, ID, dateOfBirth, isCitizen);
        this.lastVotedRound = 0;
    }

    public int getLastVotedRound() {
        return this.lastVotedRound;
    }

    public void setLastVotedRound(int value) {
        this.lastVotedRound = value;
    }
}
