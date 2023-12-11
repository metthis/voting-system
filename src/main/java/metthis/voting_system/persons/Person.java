package metthis.voting_system.persons;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person implements Comparable<Person> {
    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private Boolean isCitizen;

    public Person() {
    }

    public Person(String name, String ID, String dateOfBirth, Boolean isCitizen) {
        this.name = name;
        this.id = ID;
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
