package metthis.voting_system.persons;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Voter extends Person {

    @Column(nullable = false)
    private Integer lastVotedRound = 0;

    public Voter() {
        super();
    }

    public Voter(String name, String id, String dateOfBirth, Boolean isCitizen) {
        super(name, id, dateOfBirth, isCitizen);
    }

    public Integer getLastVotedRound() {
        return this.lastVotedRound;
    }

    public void setLastVotedRound(Integer value) {
        this.lastVotedRound = value;
    }
}
