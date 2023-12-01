package metthis.voting_system.persons;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Voter extends Person {

    @Column(nullable = false)
    private int lastVotedRound = 0;

    public Voter(String name, String ID, String dateOfBirth, boolean isCitizen) {
        super(name, ID, dateOfBirth, isCitizen);
    }

    public int getLastVotedRound() {
        return this.lastVotedRound;
    }

    public void setLastVotedRound(int value) {
        this.lastVotedRound = value;
    }
}
