package metthis.voting_system.persons;

public class Voter extends Person {
    private boolean voted;

    public Voter(String name, String ID, String dateOfBirth, boolean isCitizen) {
        super(name, ID, dateOfBirth, isCitizen);
        this.voted = false;
    }

    public boolean getVoted() {
        return this.voted;
    }

    public void voted() {
        this.voted = true;
    }
}
