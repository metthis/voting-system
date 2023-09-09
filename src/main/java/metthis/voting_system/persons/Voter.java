package metthis.voting_system.persons;

import metthis.voting_system.elections.Election;

public class Voter extends Person {
    private boolean voted;

    public Voter(String name, String ID, String dateOfBirth, boolean isCitizen) {
        super(name, ID, dateOfBirth, isCitizen);
        this.voted = false;
    }

    @Override
    public boolean isEligible(Election election) {
        return election.isEligibleVoter(this);
    }

    public boolean getVoted() {
        return this.voted;
    }

    public void voted() {
        this.voted = true;
    }
}
