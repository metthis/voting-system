package metthis.voting_system.persons;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class Voter extends Person {

    @NotNull
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

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Candidate{")
                .append(name)
                .append(", ")
                .append(id)
                .append(", ")
                .append(dateOfBirth)
                .append(", ")
                .append(isCitizen)
                .append(", ")
                .append(lastVotedRound)
                .append("}")
                .toString();
    }
}
