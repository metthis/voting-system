package metthis.voting_system.domain;

import java.time.LocalDate;

public class Candidate extends Person {
    private LocalDate applicationDate;
    private LocalDate withdrawalDate;

    public Candidate(String name, String ID, String dateOfBirth, boolean isCitizen, String applicationDate) {
        super(name, ID, dateOfBirth, isCitizen);
        this.applicationDate = LocalDate.parse(applicationDate);
        this.withdrawalDate = null;
    }

    public LocalDate getAplicationDate() {
        return this.applicationDate;
    }

    public LocalDate getWithdrawalDate() {
        return this.withdrawalDate;
    }

    public void withdraw(String date) {
        this.withdrawalDate = LocalDate.parse(date);
    }
}
