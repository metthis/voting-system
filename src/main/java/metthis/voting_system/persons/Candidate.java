package metthis.voting_system.persons;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Candidate extends Person {

    @Column(nullable = false)
    private LocalDate registrationDate;

    private LocalDate withdrawalDate = null;

    @Column(nullable = false)
    private Boolean lostThisElection = false;

    public Candidate() {
        super();
    }

    public Candidate(String name, String ID, String dateOfBirth, Boolean isCitizen, String registrationDate) {
        super(name, ID, dateOfBirth, isCitizen);
        this.registrationDate = (registrationDate == null) ? null : LocalDate.parse(registrationDate);
    }

    public LocalDate getRegistrationDate() {
        return this.registrationDate;
    }

    public LocalDate getWithdrawalDate() {
        return this.withdrawalDate;
    }

    public void setWithdrawalDate(String date) {
        this.withdrawalDate = (date == null) ? null : LocalDate.parse(date);
    }

    public Boolean getLostThisElection() {
        return this.lostThisElection;
    }

    public void setLostThisElection(Boolean value) {
        this.lostThisElection = value;
    }
}
