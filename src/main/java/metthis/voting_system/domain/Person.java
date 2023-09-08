package metthis.voting_system.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Person {
    private String name;
    private String ID;
    private LocalDate dateOfBirth;
    private boolean isCitizen;

    public Person(String name, String ID, String dateOfBirth, boolean isCitizen) {
        this.name = name;
        this.ID = ID;
        this.dateOfBirth = LocalDate.parse(dateOfBirth);
        this.isCitizen = isCitizen;
    }

    public String getName() {
        return this.name;
    }

    public String getID() {
        return this.ID;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public boolean getIsCitizen() {
        return this.isCitizen;
    }

    /*
     * Returns the time difference rounded down to whole years.
     * Eg. 17 years and 364 days is represented as 17 years.
     */
    public int getAge(LocalDate onDate) {
        return (int) this.dateOfBirth.until(onDate, ChronoUnit.YEARS);
    }
}
