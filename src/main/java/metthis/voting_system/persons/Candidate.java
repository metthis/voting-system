package metthis.voting_system.persons;

import java.time.LocalDate;

import metthis.voting_system.elections.Election;

public class Candidate extends Person {
    private LocalDate registrationDate;
    private LocalDate withdrawalDate;

    public Candidate(String name, String ID, String dateOfBirth, boolean isCitizen, String registrationDate) {
        super(name, ID, dateOfBirth, isCitizen);
        this.registrationDate = LocalDate.parse(registrationDate);
        this.withdrawalDate = null;
    }

    @Override
    public boolean isEligible(Election election) {
        return election.isEligibleCandidate(this);
    }

    public LocalDate getAplicationDate() {
        return this.registrationDate;
    }

    public LocalDate getWithdrawalDate() {
        return this.withdrawalDate;
    }

    public void withdraw(String date) {
        this.withdrawalDate = LocalDate.parse(date);
    }
}
