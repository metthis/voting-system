package metthis.voting_system.persons;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;

public class CandidateTests {
    private Candidate candidate;

    @BeforeEach
    void initEach() {
        this.candidate = new Candidate("Jane Doe", "12345678", "1995-05-03", true, "2023-03-15");
    }

    @Test
    void canGetName() {
        assertEquals("Jane Doe", this.candidate.getName());
    }

    @Test
    void canGetID() {
        assertEquals("12345678", this.candidate.getID());
    }

    @Test
    void getDateOfBirthIsInLocalDateFormat() {
        LocalDate expected = LocalDate.of(1995, 05, 03);
        assertEquals(expected, this.candidate.getDateOfBirth());
    }

    @Test
    void canGetIsCitizen() {
        assertEquals(true, this.candidate.getIsCitizen());
    }

    @Test // Should be parametrised
    void getAgeReturnsCorrectAge() {
        LocalDate date1 = LocalDate.of(2013, 05, 02);
        LocalDate date2 = LocalDate.of(2013, 05, 03);
        LocalDate date3 = LocalDate.of(2013, 05, 04);

        int age1 = this.candidate.getAge(date1);
        int age2 = this.candidate.getAge(date2);
        int age3 = this.candidate.getAge(date3);

        assertEquals(17, age1);
        assertEquals(18, age2);
        assertEquals(18, age3);
    }

    // The following are tests of methods specific to Candidate:

    @Test
    void getApplicationDateIsInLocalDateFormat() {
        LocalDate expected = LocalDate.of(2023, 03, 15);
        assertEquals(expected, this.candidate.getAplicationDate());
    }

    @Test
    void withdrawalDateIsInitiallyNull() {
        assertNull(this.candidate.getWithdrawalDate());
    }

    @Test
    void withdrawalDateCanBeSetToLocalDateFormat() {
        this.candidate.withdraw("2023-10-31");
        LocalDate expected = LocalDate.of(2023, 10, 31);
        assertEquals(expected, this.candidate.getWithdrawalDate());
    }
}
