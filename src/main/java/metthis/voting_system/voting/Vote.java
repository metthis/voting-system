package metthis.voting_system.voting;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import metthis.voting_system.persons.Candidate;

import java.util.UUID;

// This abstract class acts as a pseudo-interface for its subclasses.
// It needs to be done this way because Spring Data repositories
// don't support interfaces as "entities".

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Vote {
    @Id
    @GeneratedValue
    @JsonIgnore
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private Integer votingRound;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    @JsonIgnoreProperties({ "name", "dateOfBirth", "isCitizen", "registrationDate", "withdrawalDate",
            "lostThisElection" })
    private Candidate choice;

    public Vote(Integer votingRound, Candidate choice) {
        this.votingRound = votingRound;
        this.choice = choice;
    }

    public Vote(Integer votingRound) {
        this.votingRound = votingRound;
    }

    // To be removed when the old implementation is removed
    public Vote(Candidate choice) {
        this.choice = choice;
    }

    public Vote() {
    }

    public UUID getId() {
        return this.id;
    }

    public Integer getVotingRound() {
        return this.votingRound;
    }

    public Candidate getChoice() {
        return this.choice;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((votingRound == null) ? 0 : votingRound.hashCode());
        result = prime * result + ((choice == null) ? 0 : choice.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vote other = (Vote) obj;
        if (votingRound == null) {
            if (other.votingRound != null)
                return false;
        } else if (!votingRound.equals(other.votingRound))
            return false;
        if (choice == null) {
            if (other.choice != null)
                return false;
        } else if (!choice.equals(other.choice))
            return false;
        return true;
    }
}
