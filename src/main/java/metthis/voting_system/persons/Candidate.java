package metthis.voting_system.persons;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Candidate extends Person {

    @NotNull
    @Column(nullable = false)
    private LocalDate registrationDate;

    private LocalDate withdrawalDate = null;

    @NotNull
    @Column(nullable = false)
    private Boolean lostThisElection = false;

    public Candidate() {
        super();
    }

    public Candidate(String name, String id, String dateOfBirth, Boolean isCitizen, String registrationDate) {
        super(name, id, dateOfBirth, isCitizen);
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

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Candidate{")
                .append(name)
                .append(", ")
                .append(id)
                .append(", ")
                .append(dateOfBirth)
                .append(", ")
                .append(isCitizen)
                .append(", ")
                .append(registrationDate)
                .append(", ")
                .append(withdrawalDate)
                .append(", ")
                .append(lostThisElection)
                .append("}")
                .toString();
    }
}
