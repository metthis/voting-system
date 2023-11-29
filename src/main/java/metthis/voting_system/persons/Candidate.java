package metthis.voting_system.persons;

import java.time.LocalDate;

import jakarta.persistence.Entity;

@Entity
public class Candidate extends Person {
    private LocalDate registrationDate;

    private LocalDate withdrawalDate;

    private boolean lostThisElection;

    public Candidate(String name, String ID, String dateOfBirth, boolean isCitizen, String registrationDate) {
        super(name, ID, dateOfBirth, isCitizen);
        this.registrationDate = LocalDate.parse(registrationDate);
        this.withdrawalDate = null;
        this.lostThisElection = false;
    }

    public LocalDate getRegistrationDate() {
        return this.registrationDate;
    }

    public LocalDate getWithdrawalDate() {
        return this.withdrawalDate;
    }

    public void withdraw(String date) {
        this.withdrawalDate = LocalDate.parse(date);
    }

    public boolean getLostThisElection() {
        return this.lostThisElection;
    }

    public void setLostThisElection(boolean value) {
        this.lostThisElection = value;
    }
}
