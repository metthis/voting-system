package metthis.voting_system.voting;

import java.util.UUID;

import jakarta.persistence.Column;
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

    @Column(nullable = false)
    private Integer votingRound;

    @ManyToOne
    private Candidate choice;

    public SingleCandidateSingleChoiceVote(Integer votingRound, Candidate choice) {
        this.votingRound = votingRound;
        this.choice = choice;
    }

    public SingleCandidateSingleChoiceVote(Integer votingRound) {
        this.votingRound = votingRound;
    }

    // To be removed when the old implementation is removed
    public SingleCandidateSingleChoiceVote(Candidate choice) {
        this.choice = choice;
    }

    public SingleCandidateSingleChoiceVote() {
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
        SingleCandidateSingleChoiceVote other = (SingleCandidateSingleChoiceVote) obj;
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
