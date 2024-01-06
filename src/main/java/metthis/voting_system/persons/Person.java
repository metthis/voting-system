package metthis.voting_system.persons;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person implements Comparable<Person> {
    @Id
    @Column(nullable = false)
    protected String id;

    @NotNull
    @Column(nullable = false)
    protected String name;

    @NotNull
    @Column(nullable = false)
    protected LocalDate dateOfBirth;

    @NotNull
    @Column(nullable = false)
    protected Boolean isCitizen;

    public Person() {
    }

    public Person(String name, String id, String dateOfBirth, Boolean isCitizen) {
        this.name = name;
        this.id = id;
        this.dateOfBirth = (dateOfBirth == null) ? null : LocalDate.parse(dateOfBirth);
        this.isCitizen = isCitizen;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Boolean getIsCitizen() {
        return this.isCitizen;
    }

    /*
     * Returns the time difference rounded down to whole years.
     * Eg. 17 years and 364 days is represented as 17 years.
     */
    public int getAge(LocalDate onDate) {
        return (int) this.dateOfBirth.until(onDate, ChronoUnit.YEARS);
    }

    @Override
    public abstract String toString();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int compareTo(Person other) {
        return this.id.compareTo(other.id);
    }
}
