package metthis.voting_system.domain;

import java.time.LocalDate;

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
}
