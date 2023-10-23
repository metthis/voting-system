package metthis.voting_system.persons;

public class Voter extends Person {
    private int lastVotedRound;

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
