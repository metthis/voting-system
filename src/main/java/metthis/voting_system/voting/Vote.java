package metthis.voting_system.voting;

import metthis.voting_system.persons.Candidate;

public interface Vote {
    public Candidate getChoice();

    public Integer getVotingRound();

    public int hashCode();

    public boolean equals(Object obj);
}
