package metthis.voting_system.persons;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import metthis.voting_system.elections.Election;

public abstract class Person implements Comparable<Person> {
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

    public abstract boolean isEligible(Election election);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ID == null) ? 0 : ID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (ID == null) {
            if (other.ID != null)
                return false;
        } else if (!ID.equals(other.ID))
            return false;
        return true;
    }

    @Override
    public int compareTo(Person other) {
        return this.ID.compareTo(other.ID);
    }
}
