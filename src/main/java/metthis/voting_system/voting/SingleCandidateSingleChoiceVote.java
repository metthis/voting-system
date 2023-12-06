package metthis.voting_system.voting;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import metthis.voting_system.persons.Candidate;

@Entity
public class SingleCandidateSingleChoiceVote implements Vote {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Candidate choice;

    public SingleCandidateSingleChoiceVote(Candidate choice) {
        this.choice = choice;
    }

    public SingleCandidateSingleChoiceVote() {
        this.choice = null;
    }

    public Candidate getChoice() {
        return this.choice;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        SingleCandidateSingleChoiceVote other = (SingleCandidateSingleChoiceVote) obj;
        if (choice == null) {
            if (other.choice != null)
                return false;
        } else if (!choice.equals(other.choice))
            return false;
        return true;
    }
}
